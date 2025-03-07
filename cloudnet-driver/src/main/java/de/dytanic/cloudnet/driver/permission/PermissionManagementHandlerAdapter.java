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

package de.dytanic.cloudnet.driver.permission;

import java.util.Collection;

public class PermissionManagementHandlerAdapter implements IPermissionManagementHandler {

  @Override
  public void handleAddUser(IPermissionManagement permissionManagement, IPermissionUser permissionUser) {

  }

  @Override
  public void handleUpdateUser(IPermissionManagement permissionManagement, IPermissionUser permissionUser) {

  }

  @Override
  public void handleDeleteUser(IPermissionManagement permissionManagement, IPermissionUser permissionUser) {

  }

  @Override
  public void handleSetUsers(IPermissionManagement permissionManagement, Collection<? extends IPermissionUser> users) {

  }

  @Override
  public void handleAddGroup(IPermissionManagement permissionManagement, IPermissionGroup permissionGroup) {

  }

  @Override
  public void handleUpdateGroup(IPermissionManagement permissionManagement, IPermissionGroup permissionGroup) {

  }

  @Override
  public void handleDeleteGroup(IPermissionManagement permissionManagement, IPermissionGroup permissionGroup) {

  }

  @Override
  public void handleSetGroups(IPermissionManagement permissionManagement,
    Collection<? extends IPermissionGroup> groups) {

  }

  @Override
  public void handleReloaded(IPermissionManagement permissionManagement) {

  }
}
