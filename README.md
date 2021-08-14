# Remember Me
Remember Me is a plugin for the Velocity Minecraft Proxy that allows users to login to the server they last logged out on.

At the moment of writing the latest version of RememberMe requires Velocity 1.1 to work. Velocity 1.1 is not fully released yet but can be downloaded from their jenkins.
RememberMe versions < 1.2 can be used with the currently released version of Velocity, but have a mandatory dependency on Luckperms and have no further support.

## Permissions

* `rememberme.notransfer` - doesn't transfer the user on login, but follows the default Velocity 'try' array instead, see velocity.toml
* `rememberme.notracking` - don't track the user at all (can be used with LuckPerms contexts to ignore tracking on certain servers)
* `rememberme.ignoreforcedhosts` - ignore forced hosts and connect to the last server (otherwise forced hosts have priority)

## Integrations

### LuckPerms
When LuckPerms is installed, the user's last server is saved as a meta tag to the user with key `last-server`
