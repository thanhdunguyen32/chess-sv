package game.module.gm.logic;

import game.GameServer;
import game.entity.PlayingRole;
import game.module.hero.dao.GeneralTemplateCache;
import game.module.template.GeneralTemplate;
import game.module.user.logic.ScrollAnnoManager;
import game.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ws.WsMessageHall;
import ws.WsMessageHall.PushMarquee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Collection;

public class GmManager {

	private static Logger logger = LoggerFactory.getLogger(GmManager.class);

	static class SingletonHolder {
		static GmManager instance = new GmManager();
	}

	public static GmManager getInstance() {
		return SingletonHolder.instance;
	}

	    public void sendMarquee(final String subtitleContent, final int repeateCount) {
        try {
            logger.info("=== START SEND SCROLL ANNO ===");
            logger.info("Content: {}, RepeatCount: {}", subtitleContent, repeateCount);
            
            Collection<PlayingRole> players = SessionManager.getInstance().getAllPlayers();
            logger.info("Total online players: {}", players != null ? players.size() : 0);
            
            if (players == null || players.isEmpty()) {
                logger.warn("No online players to send scroll anno");
                return;
            }

            GameServer.executorService.execute(() -> {
                try {
                    // Sử dụng ScrollAnnoManager để gửi thông báo
                    for (int i = 0; i < repeateCount; i++) {
                        ScrollAnnoManager.getInstance().sendGmAnnouncement(subtitleContent);
                    }
                    logger.info("=== SCROLL ANNO SENT SUCCESSFULLY ===");
                } catch (Exception e) {
                    logger.error("Error in executor thread", e);
                }
            });
        } catch (Exception e) {
            logger.error("Error in sendMarquee", e);
        }
    }
	

	public void sendMarqueeTemplate(final int marquee_template_id,final int repeatCount) {
		logger.info("send marquee,marquee_template_id={}", marquee_template_id);
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
					PushMarquee pushMsg = new PushMarquee();
					pushMsg.template_id = marquee_template_id;
					pushMsg.parameters = new ArrayList<>();
					pushMsg.count = 1;
					pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
				}
			}
		});
	}
	
	public void sendPurpleCardMarquee(final int cardTemplateId, final String playerName,
			final int marquee_template_id) {
		GeneralTemplate getCardTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(cardTemplateId);
		if (getCardTemplate == null) {
			return;
		}
		int cardQuality = (int) getCardTemplate.getSTAR();
		if (cardQuality == 3) {
			logger.info("send marquee,marquee_template_id={},cardTemplateId={},playerName={}", marquee_template_id,
					cardTemplateId, playerName);
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
						PushMarquee pushMsg = new PushMarquee();
						pushMsg.template_id = marquee_template_id;
						pushMsg.parameters = Arrays.asList( playerName, String.valueOf(cardTemplateId) );
						pushMsg.count = 1;
						pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
					}
				}
			});
		}
	}

	public void sendCardStarMarquee(final int cardTemplateId, final String playerName, final int starCount) {
		GeneralTemplate getCardTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(cardTemplateId);
		if (getCardTemplate == null) {
			return;
		}
		final int marquee_template_id = 2;
		if (starCount >= 3) {
			logger.info("send marquee,marquee_template_id={},cardTemplateId={},playerName={}", marquee_template_id,
					cardTemplateId, playerName);
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
						PushMarquee pushMsg = new PushMarquee();
						pushMsg.template_id = marquee_template_id;
						pushMsg.parameters = Arrays.asList( playerName, String.valueOf(cardTemplateId),
								String.valueOf(starCount) );
						pushMsg.count = 1;
						pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
					}
				}
			});
		}
	}

	public void sendPassNormalGroupMarquee(final String playerName, int group_index, int stage_index) {
		final int marquee_template_id = 3;
		// 判断是否为当前chapter最后一关
		Map<String, Object> chapterGroupTpl = null;
		List<Map<String, Object>> stageTplList = (List<Map<String, Object>>) chapterGroupTpl.get("stage");
		if (stage_index != stageTplList.size() - 1) {
			return;
		}
		logger.info("send marquee,marquee_template_id={},group_index={},stage_index={},playerName={}",
				marquee_template_id, group_index, stage_index, playerName);
		final String groupName = (String) chapterGroupTpl.get("name");
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
					PushMarquee pushMsg = new PushMarquee();
					pushMsg.template_id = marquee_template_id;
					pushMsg.parameters = Arrays.asList( playerName, groupName );
					pushMsg.count = 1;
					pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
				}
			}
		});
	}

	public void sendArmyUpgradeMarquee(final int newGrade, final String playerName) {
		if (newGrade >= 4) {
			final int marquee_template_id = 15;
			logger.info("send marquee,marquee_template_id={},newGrade={},playerName={}", marquee_template_id, newGrade,
					playerName);
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
						PushMarquee pushMsg = new PushMarquee();
						pushMsg.template_id = marquee_template_id;
						pushMsg.parameters = Arrays.asList( playerName, String.valueOf(newGrade) );
						pushMsg.count = 1;
						pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
					}
				}
			});
		}
	}

	public void sendArenaRankMarquee(final int newRank, final String playerName) {
		if (newRank <= 10) {
			final int marquee_template_id = 16;
			logger.info("send marquee,marquee_template_id={},newRank={},playerName={}", marquee_template_id, newRank,
					playerName);
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
						PushMarquee pushMsg = new PushMarquee();
						pushMsg.template_id = marquee_template_id;
						pushMsg.parameters = Arrays.asList( playerName, String.valueOf(newRank) );
						pushMsg.count = 1;
						pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
					}
				}
			});
		}
	}
	
	public void sendBuyCoinCritMarquee(final int critRate, final String playerName) {
		if (critRate > 1) {
			final int marquee_template_id = 19;
			logger.info("send marquee,marquee_template_id={},newRank={},playerName={}", marquee_template_id, critRate,
					playerName);
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
						PushMarquee pushMsg = new PushMarquee();
						pushMsg.template_id = marquee_template_id;
						pushMsg.parameters = Arrays.asList( playerName, String.valueOf(critRate) );
						pushMsg.count = 1;
						pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
					}
				}
			});
		}
	}
	
	public void sendRunePutupMarquee(final int runeLevel, final String playerName) {
		if (runeLevel > 0) {
			final int marquee_template_id = 20;
			logger.info("send marquee,marquee_template_id={},runeLevel={},playerName={}", marquee_template_id, runeLevel,
					playerName);
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
						PushMarquee pushMsg = new PushMarquee();
						pushMsg.template_id = marquee_template_id;
						pushMsg.parameters = Arrays.asList( playerName, "+"+runeLevel );
						pushMsg.count = 1;
						pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
					}
				}
			});
		}
	}
	
	public void sendSecretBoxMarquee(final String playerName) {
		final int marquee_template_id = 21;
		logger.info("send marquee,marquee_template_id={},playerName={}", marquee_template_id, playerName);
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
					PushMarquee pushMsg = new PushMarquee();
					pushMsg.template_id = marquee_template_id;
					pushMsg.parameters = Arrays.asList( playerName );
					pushMsg.count = 1;
					pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
				}
			}
		});
	}
	
	public void sendBossBattleStartMarquee() {
		final int marquee_template_id = 22;
		sendMarqueeTemplate(marquee_template_id, 1);
	}

	public void sendBossBattleEndMarquee(final String winnerName) {
		final int marquee_template_id = 23;
		logger.info("send marquee,marquee_template_id={}", marquee_template_id);
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
					PushMarquee pushMsg = new PushMarquee();
					pushMsg.template_id = marquee_template_id;
					pushMsg.parameters = Arrays.asList( winnerName);
					pushMsg.count = 1;
					pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
				}
			}
		});
	}
	
	public void sendGoldCardMarquee(final int cardTemplateId, final String playerName) {
		GeneralTemplate getCardTemplate = GeneralTemplateCache.getInstance().getHeroTemplate(cardTemplateId);
		if (getCardTemplate == null) {
			return;
		}
		int marquee_template_id = 24;
		int cardQuality = (int) getCardTemplate.getSTAR();
		if (cardQuality == 4) {
			logger.info("send marquee,marquee_template_id={},cardTemplateId={},playerName={}", marquee_template_id,
					cardTemplateId, playerName);
			GameServer.executorService.execute(new Runnable() {
				public void run() {
					for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
						PushMarquee pushMsg = new PushMarquee();
						pushMsg.template_id = marquee_template_id;
						pushMsg.parameters = Arrays.asList( playerName, String.valueOf(cardTemplateId) );
						pushMsg.count = 1;
						pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
					}
				}
			});
		}
	}
	
	public void sendKingOnlineMarquee(final String playerName) {
		int marquee_template_id = 25;
		logger.info("send King Online Marquee,marquee_template_id={},playerName={}", marquee_template_id, playerName);
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
					PushMarquee pushMsg = new PushMarquee();
					pushMsg.template_id = marquee_template_id;
					pushMsg.parameters = Arrays.asList( playerName );
					pushMsg.count = 1;
					pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
				}
			}
		});
	}
	
	public void sendGuozhanCityPassMarquee(final String nationName) {
		int marquee_template_id = 26;
		logger.info("send guozhan city pass Marquee,marquee_template_id={},playerName={}", marquee_template_id, nationName);
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
					PushMarquee pushMsg = new PushMarquee();
					pushMsg.template_id = marquee_template_id;
					pushMsg.parameters = Arrays.asList( nationName );
					pushMsg.count = 1;
					pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
				}
			}
		});
	}
	
	public void sendActivityQZYBWinMarquee(final String playerName) {
		int marquee_template_id = 27;
		logger.info("send activity qzyb win Marquee,marquee_template_id={},playerName={}", marquee_template_id, playerName);
		GameServer.executorService.execute(new Runnable() {
			public void run() {
				for (PlayingRole pr : SessionManager.getInstance().getAllPlayers()) {
					PushMarquee pushMsg = new PushMarquee();
					pushMsg.template_id = marquee_template_id;
					pushMsg.parameters = Arrays.asList( playerName );
					pushMsg.count = 1;
					pr.getGamePlayer().writeAndFlush(pushMsg.build(pr.alloc()));
				}
			}
		});
	}

	public void handleSubtitle(String content, int repeatCount) {
		try {
			logger.info("Handling subtitle - Content: {}, RepeatCount: {}", content, repeatCount);
			sendMarquee(content, repeatCount);
		} catch (Exception e) {
			logger.error("Error handling subtitle", e);
		}
	}

	public void handlePayment(int playerId, String packageType, String packageId) {
		try {
			logger.info("Handling payment - PlayerId: {}, PackageType: {}, PackageId: {}", 
				playerId, packageType, packageId);

			PlayingRole player = SessionManager.getInstance().getPlayer(playerId);
			if (player == null) {
				logger.error("Player not found: {}", playerId);
				return;
			}

			// Xử lý thanh toán gói
			processPackagePayment(player, packageType, packageId);

		} catch (Exception e) {
			logger.error("Error handling payment", e);
		}
	}

	private void processPackagePayment(PlayingRole player, String packageType, String packageId) {
		// TODO: Implement package payment logic
		// 1. Validate package
		// 2. Check player eligibility
		// 3. Process payment
		// 4. Grant rewards
		// 5. Send notification
		logger.info("Processing package payment - Player: {}, Type: {}, Package: {}", 
			player.getId(), packageType, packageId);
	}

	public void getPackageList(String type) {
		try {
			logger.info("Getting package list - Type: {}", type);
			// TODO: Return package list based on type
			// This should be implemented based on your package system
		} catch (Exception e) {
			logger.error("Error getting package list", e);
		}
	}

	public void getPaymentStats(String type, String startDate, String endDate) {
		try {
			logger.info("Getting payment stats - Type: {}, Period: {} to {}", 
				type, startDate, endDate);
			// TODO: Return payment statistics
			// This should be implemented based on your payment tracking system
		} catch (Exception e) {
			logger.error("Error getting payment stats", e);
		}
	}

}
