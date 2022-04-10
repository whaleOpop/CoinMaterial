package coinmaterial.coinmaterial;

import coinmaterial.coinmaterial.GuildSerializer.GuildSerializer.RoleGuild;
import coinmaterial.coinmaterial.CoinSerializer.CoinSerializer;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * Implements GuildModel to save guilds data to
 * Usage:        class constructor
 * Requirements: GuildSerializer.RoleGuild
 */
public class GuildModel {
	// TODO: add online support

	public String nameGuild;
	public String prefixGuild;
	public String colorGuild;
	public HashMap<String, RoleGuild> roleGuild = new HashMap<String, RoleGuild>();

	public GuildModel(String nameGuild, String prefixGuild, String colorGuild, HashMap<String, RoleGuild> roleGuild) {
		// Simple constructor
		this.nameGuild = nameGuild;
		this.prefixGuild = prefixGuild;
		this.colorGuild = colorGuild;
		this.roleGuild = roleGuild;
	}
	
	public boolean hasPlayer(String nickname) {
		// hasPlayer method - returns true if player is in this guild
		return roleGuild.get(nickname) != null;
	}
	
	public boolean testMembership(String nickname) {
		// testMemberShip method - returns true if player is a guild Member/Operator/Creator
		RoleGuild rg = roleGuild.get(nickname);
		if (rg != null) {
			return (rg == RoleGuild.Member) || (rg == RoleGuild.Opperator) || (rg == RoleGuild.Creator);
		} else {
			return false;
		}
	}
	
	public boolean testOperatorship(String nickname) {
		// testOperatorShip method - returns true if player is a guild Operator/Creator
		RoleGuild rg = roleGuild.get(nickname);
		if (rg != null) {
			return (rg == RoleGuild.Opperator) || (rg == RoleGuild.Creator);
		} else {
			return false;
		}
	}
	
	public boolean testOwnership(String nickname) {
		// testOwnerShip method - returns true if player is a guild Creator
		RoleGuild rg = roleGuild.get(nickname);
		if (rg != null) {
			return rg == RoleGuild.Creator;
		} else {
			return false;
		}
	}
	
	public String getGuildWalletName() {
		// getGuildWalletName - returns guild name with guildPrefix in CoinSerializer
		String playerName = null;
		for (Entry<String, RoleGuild> entry : roleGuild.entrySet()) {
			if (entry.getValue() == RoleGuild.Creator) {
				playerName = CoinSerializer.guildPrefix + entry.getKey();
			}
		}
		return playerName;
	}
}
