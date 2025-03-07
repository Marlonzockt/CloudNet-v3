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

package de.dytanic.cloudnet.event.command;

import de.dytanic.cloudnet.command.ICommandSender;
import de.dytanic.cloudnet.driver.event.Event;
import de.dytanic.cloudnet.driver.event.ICancelable;

public class CommandPreProcessEvent extends Event implements ICancelable {

  private final String commandLine;

  private final ICommandSender commandSender;

  private boolean cancelled = false;

  public CommandPreProcessEvent(String commandLine, ICommandSender commandSender) {
    this.commandLine = commandLine;
    this.commandSender = commandSender;
  }

  public String getCommandLine() {
    return this.commandLine;
  }

  public ICommandSender getCommandSender() {
    return this.commandSender;
  }

  public boolean isCancelled() {
    return this.cancelled;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}
