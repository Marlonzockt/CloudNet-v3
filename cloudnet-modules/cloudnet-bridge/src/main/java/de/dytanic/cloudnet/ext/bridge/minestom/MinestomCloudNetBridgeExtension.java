/*
 * Copyright 2019-2021 CloudNetService team & contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.dytanic.cloudnet.ext.bridge.minestom;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.ServiceTask;
import de.dytanic.cloudnet.ext.bridge.BridgeConfiguration;
import de.dytanic.cloudnet.ext.bridge.BridgeConfigurationProvider;
import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.BridgePlayerManager;
import de.dytanic.cloudnet.ext.bridge.OnlyProxyProtection;
import de.dytanic.cloudnet.ext.bridge.listener.BridgeCustomChannelMessageListener;
import de.dytanic.cloudnet.ext.bridge.minestom.listener.MinestomCloudNetListener;
import de.dytanic.cloudnet.ext.bridge.player.IPlayerManager;
import de.dytanic.cloudnet.ext.bridge.server.BridgeServerHelper;
import de.dytanic.cloudnet.wrapper.Wrapper;
import java.time.Duration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.AsyncPlayerPreLoginEvent;
import net.minestom.server.event.server.ServerListPingEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.ping.ResponseData;
import net.minestom.server.ping.ServerListPingType;

public final class MinestomCloudNetBridgeExtension extends Extension {

  private final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacy('&');
  private OnlyProxyProtection onlyProxyProtection;

  @Override
  public void initialize() {
    MinestomCloudNetHelper.init();

    CloudNetDriver.getInstance().getServicesRegistry()
      .registerService(IPlayerManager.class, "BridgePlayerManager", new BridgePlayerManager());

    this.initListeners();

    Wrapper.getInstance().getTaskExecutor().execute(BridgeHelper::updateServiceInfo);

    this.runFireServerListPingEvent();
  }

  @Override
  public void terminate() {
    CloudNetDriver.getInstance().getEventManager().unregisterListeners(this.getClass().getClassLoader());
    Wrapper.getInstance().unregisterPacketListenersByClassLoader(this.getClass().getClassLoader());
  }

  private void initListeners() {
    onlyProxyProtection = new OnlyProxyProtection(MojangAuth.isEnabled());
    //Minestom
    getEventNode().addListener(AsyncPlayerPreLoginEvent.class, event -> {
      Player player = event.getPlayer();
      BridgeConfiguration bridgeConfiguration = BridgeConfigurationProvider.load();

      if (this.onlyProxyProtection.shouldDisallowPlayer(event.getPlayer().getPlayerConnection().getServerAddress())) {
        event.getPlayer().kick(serializer
          .deserialize(bridgeConfiguration.getMessages().get("server-join-cancel-because-only-proxy")));
        return;
      }

      String currentTaskName = Wrapper.getInstance().getServiceId().getTaskName();
      ServiceTask serviceTask = Wrapper.getInstance().getServiceTaskProvider().getServiceTask(currentTaskName);

      if (serviceTask != null) {
        String requiredPermission = serviceTask.getProperties().getString("requiredPermission");
        if (requiredPermission != null && !player.hasPermission(requiredPermission)) {
          event.getPlayer().kick(serializer
            .deserialize(bridgeConfiguration.getMessages().get("server-join-cancel-because-permission")));
          return;
        }

        if (serviceTask.isMaintenance() && !player.hasPermission("cloudnet.bridge.maintenance")) {
          event.getPlayer().kick(serializer
            .deserialize(bridgeConfiguration.getMessages().get("server-join-cancel-because-maintenance")));
          return;
        }
      }

      BridgeHelper.sendChannelMessageServerLoginRequest(MinestomCloudNetHelper.createNetworkConnectionInfo(player),
        MinestomCloudNetHelper.createNetworkPlayerServerInfo(player, true)
      );
    });

    //CloudNet
    CloudNetDriver.getInstance().getEventManager().registerListener(new MinestomCloudNetListener());
    CloudNetDriver.getInstance().getEventManager().registerListener(new BridgeCustomChannelMessageListener());
  }

  private void runFireServerListPingEvent() {
    MinecraftServer.getSchedulerManager().buildTask(() -> {
      boolean hasToUpdate = false;
      boolean ingame = false;

      try {
        ServerListPingEvent serverListPingEvent = new ServerListPingEvent(ServerListPingType.MODERN_FULL_RGB);
        MinecraftServer.getGlobalEventHandler().call(serverListPingEvent);

        final ResponseData responseData = serverListPingEvent.getResponseData();
        BridgeServerHelper.setMotd(serializer.serialize(responseData.getDescription()));

        String lowerMotd = PlainTextComponentSerializer.plainText().serialize(responseData.getDescription());
        if (lowerMotd.contains("running") || lowerMotd.contains("ingame") || lowerMotd.contains("playing")) {
          ingame = true;
        }

        if (responseData.getMaxPlayer() != BridgeServerHelper.getMaxPlayers()) {
          hasToUpdate = true;
          BridgeServerHelper.setMaxPlayers(responseData.getMaxPlayer());
        }

        if (ingame) {
          BridgeServerHelper.changeToIngame(true);
          return;
        }

        if (hasToUpdate) {
          BridgeHelper.updateServiceInfo();
        }
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }).repeat(Duration.ofMillis(500));
  }
}
