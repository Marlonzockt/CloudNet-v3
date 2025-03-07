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

package de.dytanic.cloudnet.examples;

import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.module.ModuleLifeCycle;
import de.dytanic.cloudnet.driver.module.ModuleTask;
import de.dytanic.cloudnet.driver.module.driver.DriverModule;

public final class ExampleModule extends
  DriverModule { //Defines the module class for a driver based module. It can be used for a wrapper or node instance

  @ModuleTask(event = ModuleLifeCycle.LOADED)
  //Defines a module task method. You can create more as one module task for one event
  public void printLoadMessage() {
    System.out.println("Module is successfully loaded");
  }

  @ModuleTask(event = ModuleLifeCycle.STARTED)
  public void initConfig() {
    // Init the configurations/default configurations
    this.getConfig().getString("Host", "127.0.0.1");
    this.getConfig().getInt("Port", 3306);
    this.getConfig().getString("Database", "Network");
    this.getConfig().getString("Username", "root");
    this.getConfig().getString("Password", "pw123");

    this.saveConfig();
  }

  @ModuleTask(event = ModuleLifeCycle.STARTED) //important section, because on this event the module will start
  public void registerListeners() {
    CloudNetDriver.getInstance().getEventManager()
      .registerListener(new ExampleListener()); //Register a listener object on the event manager
  }

  @ModuleTask(event = ModuleLifeCycle.STOPPED) //important section, because on this event the module will stop
  public void stopModule() {
    this.getEventManager().callEvent("test_channel", new ExampleOwnEvent(this.getModuleWrapper()));
    //call own event on a specific event channel. All listeners which listen on this channel can handle with this object
  }

  @ModuleTask(event = ModuleLifeCycle.UNLOADED) // Will call, if the module will unload from runtime
  public void unloadModule() {
  }
}
