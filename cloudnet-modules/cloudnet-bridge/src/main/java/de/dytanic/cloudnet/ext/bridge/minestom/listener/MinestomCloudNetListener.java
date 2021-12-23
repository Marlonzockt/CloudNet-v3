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

package de.dytanic.cloudnet.ext.bridge.minestom.listener;

import de.dytanic.cloudnet.driver.event.EventListener;
import de.dytanic.cloudnet.driver.event.events.channel.ChannelMessageReceiveEvent;
import de.dytanic.cloudnet.driver.event.events.network.NetworkChannelPacketReceiveEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceConnectNetworkEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceDisconnectNetworkEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceInfoUpdateEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceRegisterEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceStartEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceStopEvent;
import de.dytanic.cloudnet.driver.event.events.service.CloudServiceUnregisterEvent;
import de.dytanic.cloudnet.ext.bridge.event.BridgeConfigurationUpdateEvent;
import de.dytanic.cloudnet.ext.bridge.event.BridgeProxyPlayerDisconnectEvent;
import de.dytanic.cloudnet.ext.bridge.event.BridgeProxyPlayerLoginRequestEvent;
import de.dytanic.cloudnet.ext.bridge.event.BridgeProxyPlayerLoginSuccessEvent;
import de.dytanic.cloudnet.ext.bridge.event.BridgeProxyPlayerServerConnectRequestEvent;
import de.dytanic.cloudnet.ext.bridge.event.BridgeProxyPlayerServerSwitchEvent;
import de.dytanic.cloudnet.ext.bridge.event.BridgeServerPlayerDisconnectEvent;
import de.dytanic.cloudnet.ext.bridge.event.BridgeServerPlayerLoginRequestEvent;
import de.dytanic.cloudnet.ext.bridge.event.BridgeServerPlayerLoginSuccessEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.MinestomCloudNetHelper;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomBridgeConfigurationUpdateEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomBridgeProxyPlayerDisconnectEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomBridgeProxyPlayerLoginSuccessEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomBridgeProxyPlayerServerConnectRequestEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomBridgeProxyPlayerServerSwitchEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomBridgeServerPlayerDisconnectEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomBridgeServerPlayerLoginRequestEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomBridgeServerPlayerLoginSuccessEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomChannelMessageReceiveEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomCloudServiceConnectNetworkEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomCloudServiceDisconnectNetworkEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomCloudServiceInfoUpdateEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomCloudServiceRegisterEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomCloudServiceStartEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomCloudServiceStopEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomCloudServiceUnregisterEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomNetworkChannelPacketReceiveEvent;
import de.dytanic.cloudnet.ext.bridge.minestom.event.MinestomServiceInfoSnapshotConfigureEvent;
import de.dytanic.cloudnet.wrapper.event.service.ServiceInfoSnapshotConfigureEvent;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;

public final class MinestomCloudNetListener {

  @EventListener
  public void handle(ServiceInfoSnapshotConfigureEvent event) {
    MinestomCloudNetHelper.initProperties(event.getServiceInfoSnapshot());
    this.minestomCall(new MinestomServiceInfoSnapshotConfigureEvent(event.getServiceInfoSnapshot()));
  }

  @EventListener
  public void handle(CloudServiceInfoUpdateEvent event) {
    this.minestomCall(new MinestomCloudServiceInfoUpdateEvent(event.getServiceInfo()));
  }

  @EventListener
  public void handle(CloudServiceRegisterEvent event) {
    this.minestomCall(new MinestomCloudServiceRegisterEvent(event.getServiceInfo()));
  }

  @EventListener
  public void handle(CloudServiceStartEvent event) {
    this.minestomCall(new MinestomCloudServiceStartEvent(event.getServiceInfo()));
  }

  @EventListener
  public void handle(CloudServiceConnectNetworkEvent event) {
    this.minestomCall(new MinestomCloudServiceConnectNetworkEvent(event.getServiceInfo()));
  }

  @EventListener
  public void handle(CloudServiceDisconnectNetworkEvent event) {
    this.minestomCall(new MinestomCloudServiceDisconnectNetworkEvent(event.getServiceInfo()));
  }

  @EventListener
  public void handle(CloudServiceStopEvent event) {
    this.minestomCall(new MinestomCloudServiceStopEvent(event.getServiceInfo()));
  }

  @EventListener
  public void handle(CloudServiceUnregisterEvent event) {
    this.minestomCall(new MinestomCloudServiceUnregisterEvent(event.getServiceInfo()));
  }

  @EventListener
  public void handle(ChannelMessageReceiveEvent event) {
    this.minestomCall(new MinestomChannelMessageReceiveEvent(event));
  }

  @EventListener
  public void handle(NetworkChannelPacketReceiveEvent event) {
    this.minestomCall(new MinestomNetworkChannelPacketReceiveEvent(event.getChannel(), event.getPacket()));
  }

  @EventListener
  public void handle(BridgeConfigurationUpdateEvent event) {
    this.minestomCall(new MinestomBridgeConfigurationUpdateEvent(event.getBridgeConfiguration()));
  }

  @EventListener
  public void handle(BridgeProxyPlayerLoginRequestEvent event) {
    this.minestomCall(new MinestomBridgeProxyPlayerLoginSuccessEvent(event.getNetworkConnectionInfo()));
  }

  @EventListener
  public void handle(BridgeProxyPlayerLoginSuccessEvent event) {
    this.minestomCall(new MinestomBridgeProxyPlayerLoginSuccessEvent(event.getNetworkConnectionInfo()));
  }

  @EventListener
  public void handle(BridgeProxyPlayerServerConnectRequestEvent event) {
    this.minestomCall(new MinestomBridgeProxyPlayerServerConnectRequestEvent(event.getNetworkConnectionInfo(),
      event.getNetworkServiceInfo()));
  }

  @EventListener
  public void handle(BridgeProxyPlayerServerSwitchEvent event) {
    this.minestomCall(
      new MinestomBridgeProxyPlayerServerSwitchEvent(event.getNetworkConnectionInfo(), event.getNetworkServiceInfo()));
  }

  @EventListener
  public void handle(BridgeProxyPlayerDisconnectEvent event) {
    this.minestomCall(new MinestomBridgeProxyPlayerDisconnectEvent(event.getNetworkConnectionInfo()));
  }

  @EventListener
  public void handle(BridgeServerPlayerLoginRequestEvent event) {
    this.minestomCall(new MinestomBridgeServerPlayerLoginRequestEvent(event.getNetworkConnectionInfo(),
      event.getNetworkPlayerServerInfo()));
  }

  @EventListener
  public void handle(BridgeServerPlayerLoginSuccessEvent event) {
    this.minestomCall(new MinestomBridgeServerPlayerLoginSuccessEvent(event.getNetworkConnectionInfo(),
      event.getNetworkPlayerServerInfo()));
  }

  @EventListener
  public void handle(BridgeServerPlayerDisconnectEvent event) {
    this.minestomCall(new MinestomBridgeServerPlayerDisconnectEvent(event.getNetworkConnectionInfo(),
      event.getNetworkPlayerServerInfo()));
  }

  private void minestomCall(Event event) {
    MinecraftServer.getGlobalEventHandler().call(event);
  }

}
