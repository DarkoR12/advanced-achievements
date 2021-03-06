package com.hm.achievement.listener.statistics;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import com.hm.achievement.category.NormalAchievements;
import com.hm.achievement.db.CacheManager;
import com.hm.achievement.utils.RewardParser;

/**
 * Listener class to deal with MaxLevel achievements.
 * 
 * @author Pyves
 *
 */
@Singleton
public class LevelsListener extends AbstractListener {

	@Inject
	public LevelsListener(@Named("main") YamlConfiguration mainConfig, int serverVersion,
			Map<String, List<Long>> sortedThresholds, CacheManager cacheManager, RewardParser rewardParser) {
		super(NormalAchievements.LEVELS, mainConfig, serverVersion, sortedThresholds, cacheManager, rewardParser);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerExpChange(PlayerLevelChangeEvent event) {
		Player player = event.getPlayer();

		int previousMaxLevel = (int) cacheManager.getAndIncrementStatisticAmount(NormalAchievements.LEVELS,
				player.getUniqueId(), 0);
		if (event.getNewLevel() <= previousMaxLevel) {
			return;
		}

		updateStatisticAndAwardAchievementsIfAvailable(player, event.getNewLevel() - previousMaxLevel);
	}
}
