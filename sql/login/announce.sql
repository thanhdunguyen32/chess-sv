# ************************************************************
# Sequel Ace SQL dump
# Version 20080
#
# https://sequel-ace.com/
# https://github.com/Sequel-Ace/Sequel-Ace
#
# Host: 172.104.47.85 (MySQL 8.0.40)
# Database: gg04
# Generation Time: 2025-01-11 18:34:00 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
SET NAMES utf8mb4;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE='NO_AUTO_VALUE_ON_ZERO', SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table announce
# ------------------------------------------------------------

DROP TABLE IF EXISTS `announce`;

CREATE TABLE `announce` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `content` varchar(16382) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

LOCK TABLES `announce` WRITE;
/*!40000 ALTER TABLE `announce` DISABLE KEYS */;

INSERT INTO `announce` (`id`, `content`)
VALUES
	(1,'Game \"Quân Sư 3Q\" đã chính thức mở cửa thử nghiệm không xóa dữ liệu trên tất cả các nền tảng. Cảm ơn sự ủng hộ và yêu thích của bạn dành cho game của chúng tôi!\nĐể thảo luận về đội hình, mời bạn tham gia các nhóm QQ:\n- Nhóm người chơi 1: 123456789 (đã đầy)\n- Nhóm người chơi 2: 987654321\nTheo dõi diễn đàn Baidu Tieba: \"Tam Quốc 10V10\" để xem thêm nhiều hướng dẫn.\nTài khoản công khai QQ của bộ phận Chăm sóc khách hàng:\nViệc nạp tiền trong \"Quân Sư 3Q\" chỉ có thể thực hiện thông qua kênh chính thức, không có bất kỳ kênh nạp tiền bên thứ ba nào. Để bảo đảm an toàn cho tài khoản của mình, các chủ công không nên tin tưởng vào bất kỳ kênh nạp tiền không chính thức nào, tránh rơi vào bẫy của những kẻ xấu.\nĐồng thời, chúng tôi khuyến nghị những người chơi đang sử dụng tài khoản khách nên liên kết số điện thoại để chuyển sang đăng nhập bằng tài khoản chính thức. Nếu gặp bất kỳ vấn đề nào về đăng nhập hoặc các vấn đề khác trong game, vui lòng liên hệ ngay với tài khoản công khai QQ của bộ phận Chăm sóc khách hàng.'),
	(2,'Thông tin máy chủ mới:\n\n\nMáy chủ 84 - Đao Thương Lâm Lập: Mở ngày 29/5/2020 lúc 09:40\nMáy chủ 85 - Kiếm Kích Sâm Sâm: Mở ngày 01/6/2020 lúc 09:40'),
	(3,'[Nội dung cập nhật phiên bản]\n\n1) Sửa lỗi kỹ năng chủ động của Mã Lương khi phát động không nhắm vào tướng đồng minh có tỷ lệ máu thấp nhất.\n\n2) Tối ưu hóa giao diện danh sách bạn bè, ưu tiên hiển thị những người bạn có thời gian offline ngắn hơn.'),
	(4,'Nội dung hoạt động tuần này: Ra mắt hoạt động Điểm Tướng tuần\n\n[Gói quà giá trị]\n- Trong thời gian diễn ra sự kiện, mua gói quà lễ hội sẽ nhận được nhiều phần thưởng, mỗi gói quà giới hạn mua 5 lần.\n\n[Vật phẩm quý giá trị]\n- Trong thời gian diễn ra sự kiện, mua gói quà sẽ nhận được nhiều phần thưởng, mỗi gói quà giới hạn mua 1 lần.\n\n[Điểm Điểm Tướng]\n- Trong thời gian diễn ra sự kiện, mỗi lần Điểm Tướng sẽ nhận được 1 điểm, khi đạt đến mục tiêu điểm chỉ định sẽ nhận được phần thưởng phong phú.\n\n[Nhiệm vụ Điểm Tướng]\n- Trong thời gian diễn ra sự kiện, hoàn thành nhiệm vụ chỉ định sẽ nhận được phần thưởng tương ứng (tướng nhận được thông qua đổi ở Điểm Tướng Đài không được tính).\n\n[Rương báu bí ẩn]\n- Trong thời gian diễn ra sự kiện, nhận được điểm thông qua Điểm Tướng, đổi tướng, mua gói quà. Khi đạt đến mục tiêu điểm sẽ nhận được phần thưởng phong phú.');

/*!40000 ALTER TABLE `announce` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
