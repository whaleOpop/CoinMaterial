# CoinMaterial ![Coin image](./src/main/resources/coin.png "Coin")
A DoubleWhale plugin for Minecraft servers on Bukkit / Spigot / Paper / etc.

## Description
Easy to deploy server-wide wallet system for a single common currency.

## Features
* Turn any item drops into coins on pickup by player.
* Withdraw coins to inventory as items.
* Pay other players wallet-to-wallet even when they are offline.
* L10N support with lines in [config.yml][config_file].
* Seamless integration with other DoubleWhale plugins:
    * [Guilded][guilded_repo] - guild wallets, paid guild creation, maintaining, players accept.
* Funny Arabic memes in Russian included.

## Usage
### Plugin commands
> From [plugin.yml](src/main/resources/plugin.yml):
>> Reload plugin config file:<br>
>> ```/coinmaterial reload```<br><br>
>> View wallet balance in coins:<br>
>> ```/money```<br><br>
>> Transfer ```<amount>```  of coins from wallet to other player with nickname ```<playerName>```.<br>
>> ```<playerName>```  is completed/tab completed with list of all players, who have wallets.<br>
>> ```/pay <amount> <playerName>```<br><br>
>> Withdraw ```<amount>``` of coins from wallet to inventory in items.<br>
>> If ```<amount>``` is string ```all```, then withdraw all coins in the wallet.<br>
>> Coin ItemMaterial is configured in [config.yml][config_file] in ```COIN_MATERIAL:```  string.<br>
>> ```/wallet <amount>```<br>
>> ```/wallet all```

### Any DoubleWhale plugins integration
Integrations with our plugins is under-the-hood if all plugins are in one directory, for example:<br>
<pre>
ServerRootDir/
    plugins/
        CoinMaterial.jar
	Guilded.jar
	...
</pre>
The code behind integration scans current directory of CoinMaterial.jar file for other DW plugins.<br>
Then other plugin relative commands will be available.<br>

__CoinMaterial Guilded integration error: read further:__ <br>
CoinMaterial might have not found Guilded .jar file. The main reason this might have happened is because you have Guilded plugin renamed. Please put string "Guilded" (case insensitive) in the name of the .jar file.

__You can choose to disable integrations:__ <br>
In case you want to disable plugin integrations, set ```integrateDW```  in [config.yml][config_file] under the ```settings:general:```  label with string ```"disabled"```.

### Supported DoubleWhale plugins
As for now, the only supported plugin is [Guilded][guilded_repo].

### Integration related commands
> With Guilded:<br>
>> Transfer ```<amount>```  of coins from wallet to guild's wallet.<br>
>> The guild, which wallet will be referenced is determined via player with given ```<playerName>```  with __creator/operator/member__ role in that guild .<br>
>> ```/pay guild <amount> <playerName>```<br><br>
>> Get guild's wallet balance.<br>
>> The referenced guild is the player's guild, if he has __creator/operator/member__ role in it.<br>
>> ```/money guild```<br><br>
>> Withdraw ```<amount>```  of coins from guild's wallet.<br>
>> Coins will be withdrawn if the player issuing this command has __creator/operator__ role in that guild.<br>
>> ```/wallet guild <amount>```

## Contribution
View [CONTRIBUTION.md](CONTRIBUTION.md).

[guilded_repo]: https://github.com/WhaleOpop/Guilded
[config_file]: ./src/main/resources/config.yml
