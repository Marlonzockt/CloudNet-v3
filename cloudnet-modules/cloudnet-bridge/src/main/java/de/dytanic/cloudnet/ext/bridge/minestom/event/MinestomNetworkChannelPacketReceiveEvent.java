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

package de.dytanic.cloudnet.ext.bridge.minestom.event;

import de.dytanic.cloudnet.driver.network.INetworkChannel;
import de.dytanic.cloudnet.driver.network.protocol.IPacket;

/**
 * {@inheritDoc}
 */
public final class MinestomNetworkChannelPacketReceiveEvent extends
  MinestomCloudNetEvent {

  private final INetworkChannel channel;

  private final IPacket packet;

  public MinestomNetworkChannelPacketReceiveEvent(INetworkChannel channel, IPacket packet) {
    this.channel = channel;
    this.packet = packet;
  }

  public INetworkChannel getChannel() {
    return this.channel;
  }

  public IPacket getPacket() {
    return this.packet;
  }
}
