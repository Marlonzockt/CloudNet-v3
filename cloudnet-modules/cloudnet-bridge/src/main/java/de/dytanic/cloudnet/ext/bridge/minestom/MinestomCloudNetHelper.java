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

import com.google.common.base.Preconditions;
import de.dytanic.cloudnet.driver.network.HostAndPort;
import de.dytanic.cloudnet.driver.service.ServiceInfoSnapshot;
import de.dytanic.cloudnet.ext.bridge.BridgeHelper;
import de.dytanic.cloudnet.ext.bridge.WorldPosition;
import de.dytanic.cloudnet.ext.bridge.player.NetworkConnectionInfo;
import de.dytanic.cloudnet.ext.bridge.player.NetworkPlayerServerInfo;
import de.dytanic.cloudnet.ext.bridge.server.BridgeServerHelper;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.extras.MojangAuth;

public class MinestomCloudNetHelper extends BridgeServerHelper {

  public static void init() {
    BridgeServerHelper.setMotd("");
    BridgeServerHelper.setState("LOBBY");
    BridgeServerHelper.setMaxPlayers(1);
  }

  public static void initProperties(ServiceInfoSnapshot serviceInfoSnapshot) {
    Preconditions.checkNotNull(serviceInfoSnapshot);

    Collection<MinestomCloudNetPlayerInfo> players = new ArrayList<>();
    MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(player -> {
      final Pos location = player.getPosition();

      players.add(new MinestomCloudNetPlayerInfo(
        player.getUuid(),
        player.getUsername(),
        player.getHealth(),
        player.getMaxHealth(),
        player.getFood(),
        player.getLevel(),
        new WorldPosition(
          location.x(),
          location.y(),
          location.z(),
          location.yaw(),
          location.pitch(),
          player.getInstance().getUniqueId().toString()
        ),
        new HostAndPort(((InetSocketAddress) player.getPlayerConnection().getRemoteAddress()))
      ));
    });

    serviceInfoSnapshot.getProperties()
      .append("Online", BridgeHelper.isOnline())
      .append("Version", MinecraftServer.VERSION_NAME)
      .append("Online-Count", MinecraftServer.getConnectionManager().getOnlinePlayers().size())
      .append("State", BridgeServerHelper.getState())
      .append("Online-Mode", MojangAuth.isEnabled())
      .append("Players", players);
  }

  public static NetworkConnectionInfo createNetworkConnectionInfo(Player player) {
    return BridgeHelper.createNetworkConnectionInfo(
      player.getUuid(),
      player.getUsername(),
      -1,
      new HostAndPort(((InetSocketAddress) player.getPlayerConnection().getRemoteAddress())),
      new HostAndPort(MinecraftServer.getServer().getAddress(), MinecraftServer.getServer().getPort()),
      MojangAuth.isEnabled(),
      false,
      BridgeHelper.createOwnNetworkServiceInfo()
    );
  }

  public static NetworkPlayerServerInfo createNetworkPlayerServerInfo(Player player, boolean login) {
    WorldPosition worldPosition;

    if (login) {
      worldPosition = new WorldPosition(-1, -1, -1, -1, -1, "world");
    } else {
      Pos location = player.getPosition();
      worldPosition = new WorldPosition(
        location.x(),
        location.y(),
        location.z(),
        location.yaw(),
        location.pitch(),
        player.getInstance().getUniqueId().toString()
      );
    }

    return new NetworkPlayerServerInfo(
      player.getUuid(),
      player.getUsername(),
      null,
      player.getHealth(),
      player.getMaxHealth(),
      player.getFoodSaturation(),
      player.getLevel(),
      worldPosition,
      new HostAndPort(((InetSocketAddress) player.getPlayerConnection().getRemoteAddress())),
      BridgeHelper.createOwnNetworkServiceInfo()
    );
  }
}
