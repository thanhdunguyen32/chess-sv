-- Cấu hình quyền cho root
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'gg04-3qchess';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' WITH GRANT OPTION;
FLUSH PRIVILEGES;

-- Tạo databases
CREATE DATABASE IF NOT EXISTS login_db;
CREATE DATABASE IF NOT EXISTS game_db;

-- Tạo users và cấp quyền
CREATE USER IF NOT EXISTS 'login_user'@'%' IDENTIFIED BY 'gg04-3qchess-login';
GRANT ALL PRIVILEGES ON login_db.* TO 'login_user'@'%';

CREATE USER IF NOT EXISTS 'game_user'@'%' IDENTIFIED BY 'gg04-3qchess-game';
GRANT ALL PRIVILEGES ON game_db.* TO 'game_user'@'%';

FLUSH PRIVILEGES;

-- Import login_db
USE login_db;
SOURCE /home/sql/login/announce.sql;
SOURCE /home/sql/login/login_account.sql;
SOURCE /home/sql/login/server_has_role.sql;
SOURCE /home/sql/login/server_list.sql;

-- Import game_db
USE game_db;
SOURCE /home/sql/game/ALL.sql;
-- SOURCE /home/sql/game/act_mjbg.sql;
-- SOURCE /home/sql/game/act_tnqw.sql;
-- SOURCE /home/sql/game/activity_xiangou.sql;
-- SOURCE /home/sql/game/activity.sql;
-- SOURCE /home/sql/game/affair.sql;
-- SOURCE /home/sql/game/battle_formation.sql;
-- SOURCE /home/sql/game/cdkey.sql;
-- SOURCE /home/sql/game/chapter.sql;
-- SOURCE /home/sql/game/charge.sql;
-- SOURCE /home/sql/game/dungeon.sql;
-- SOURCE /home/sql/game/exped.sql;
-- SOURCE /home/sql/game/friend_boss.sql;
-- SOURCE /home/sql/game/friend_expoler.sql;
-- SOURCE /home/sql/game/friend_request.sql;
-- SOURCE /home/sql/game/friend.sql;
-- SOURCE /home/sql/game/general_exchange.sql;
-- SOURCE /home/sql/game/gold_buy.sql;
-- SOURCE /home/sql/game/guozhan_self.sql;
-- SOURCE /home/sql/game/guozhan.sql;
-- SOURCE /home/sql/game/hero.sql;
-- SOURCE /home/sql/game/item.sql;
-- SOURCE /home/sql/game/king_pvp.sql;
-- SOURCE /home/sql/game/legion.sql;
-- SOURCE /home/sql/game/libao_buy.sql;
-- SOURCE /home/sql/game/log_item_go.sql;
-- SOURCE /home/sql/game/log_login.sql;
-- SOURCE /home/sql/game/log_topup_feedback.sql;
-- SOURCE /home/sql/game/log_topup.sql;
-- SOURCE /home/sql/game/mail.sql;
-- SOURCE /home/sql/game/manor.sql;
-- SOURCE /home/sql/game/map_event.sql;
-- SOURCE /home/sql/game/mine.sql;
-- SOURCE /home/sql/game/mission_achieve.sql;
-- SOURCE /home/sql/game/mission_daily.sql;
-- SOURCE /home/sql/game/month_boss.sql;
-- SOURCE /home/sql/game/mythical_animal.sql;
-- SOURCE /home/sql/game/occ_task.sql;
-- SOURCE /home/sql/game/online_gift.sql;
-- SOURCE /home/sql/game/player_head.sql;
-- SOURCE /home/sql/game/player_hide.sql;
-- SOURCE /home/sql/game/player_other.sql;
-- SOURCE /home/sql/game/player_server_prop.sql;
-- SOURCE /home/sql/game/player.sql;
-- SOURCE /home/sql/game/power_formation.sql;
-- SOURCE /home/sql/game/pub_draw.sql;
-- SOURCE /home/sql/game/pvp_draw.sql;
-- SOURCE /home/sql/game/pvp_record.sql;
-- SOURCE /home/sql/game/pvp.sql;
-- SOURCE /home/sql/game/session.sql;
-- SOURCE /home/sql/game/secret.sql;
-- SOURCE /home/sql/game/shop.sql;
-- SOURCE /home/sql/game/sign_in.sql;
-- SOURCE /home/sql/game/surrender.sql;
-- SOURCE /home/sql/game/system_blob.sql;
-- SOURCE /home/sql/game/tcdk.sql;
-- SOURCE /home/sql/game/tower_replay.sql;
-- SOURCE /home/sql/game/world_boss.sql;

-- Thêm các file game SQL khác tương ứng