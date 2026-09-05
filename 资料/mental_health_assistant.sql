/*
 Navicat Premium Dump SQL

 Source Server         : mysql8.0
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : mental_health_assistant

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 15/11/2025 11:15:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ai_analysis_task
-- ----------------------------
DROP TABLE IF EXISTS `ai_analysis_task`;
CREATE TABLE `ai_analysis_task`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `diary_id` bigint NOT NULL COMMENT '日记ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务状态：PENDING-待处理，PROCESSING-处理中，COMPLETED-已完成，FAILED-失败',
  `task_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '任务类型：AUTO-自动触发，MANUAL-手动触发，ADMIN-管理员触发，BATCH-批量触发',
  `priority` int NOT NULL DEFAULT 2 COMMENT '优先级：1-低，2-正常，3-高，4-紧急',
  `retry_count` int NOT NULL DEFAULT 0 COMMENT '重试次数',
  `max_retry_count` int NOT NULL DEFAULT 3 COMMENT '最大重试次数',
  `error_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误信息',
  `started_at` datetime NULL DEFAULT NULL COMMENT '处理开始时间',
  `completed_at` datetime NULL DEFAULT NULL COMMENT '处理完成时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_diary_id`(`diary_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_task_type`(`task_type` ASC) USING BTREE,
  INDEX `idx_priority`(`priority` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE,
  INDEX `idx_status_priority`(`status` ASC, `priority` ASC) USING BTREE,
  INDEX `idx_status_created_at`(`status` ASC, `created_at` ASC) USING BTREE,
  INDEX `idx_task_type_created_at`(`task_type` ASC, `created_at` ASC) USING BTREE,
  INDEX `idx_retry_status`(`status` ASC, `retry_count` ASC, `max_retry_count` ASC) USING BTREE,
  CONSTRAINT `fk_ai_task_diary` FOREIGN KEY (`diary_id`) REFERENCES `emotion_diary` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_ai_task_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = 'AI分析任务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ai_analysis_task
-- ----------------------------
INSERT INTO `ai_analysis_task` VALUES (32, 8, 2, 'COMPLETED', 'AUTO', 2, 0, 3, NULL, '2025-09-14 09:42:34', '2025-09-14 09:42:44', '2025-09-14 09:42:34', '2025-09-14 09:42:44');
INSERT INTO `ai_analysis_task` VALUES (33, 8, 2, 'COMPLETED', 'AUTO', 2, 0, 3, NULL, '2025-09-14 14:04:32', '2025-09-14 14:04:37', '2025-09-14 14:04:32', '2025-09-14 14:04:37');
INSERT INTO `ai_analysis_task` VALUES (34, 5, 2, 'COMPLETED', 'ADMIN', 3, 0, 3, NULL, '2025-09-14 14:05:48', '2025-09-14 14:05:52', '2025-09-14 14:05:48', '2025-09-14 14:05:52');
INSERT INTO `ai_analysis_task` VALUES (35, 8, 2, 'COMPLETED', 'AUTO', 2, 0, 3, NULL, '2025-09-14 14:13:21', '2025-09-14 14:13:26', '2025-09-14 14:13:21', '2025-09-14 14:13:26');
INSERT INTO `ai_analysis_task` VALUES (36, 8, 2, 'COMPLETED', 'ADMIN', 3, 0, 3, NULL, '2025-09-14 14:14:21', '2025-09-14 14:14:45', '2025-09-14 14:14:21', '2025-09-14 14:14:45');
INSERT INTO `ai_analysis_task` VALUES (37, 26, 2, 'COMPLETED', 'AUTO', 2, 0, 3, NULL, '2025-11-10 13:00:34', '2025-11-10 13:00:34', '2025-11-10 13:00:34', '2025-11-10 13:00:34');
INSERT INTO `ai_analysis_task` VALUES (38, 26, 2, 'COMPLETED', 'ADMIN', 3, 0, 3, NULL, '2025-11-10 13:02:16', '2025-11-10 13:02:16', '2025-11-10 13:02:16', '2025-11-10 13:02:16');
INSERT INTO `ai_analysis_task` VALUES (39, 26, 2, 'COMPLETED', 'AUTO', 2, 0, 3, NULL, '2025-11-10 19:05:55', '2025-11-10 19:06:00', '2025-11-10 19:05:55', '2025-11-10 19:06:00');
INSERT INTO `ai_analysis_task` VALUES (40, 26, 2, 'COMPLETED', 'AUTO', 2, 0, 3, NULL, '2025-11-10 19:06:00', '2025-11-10 19:06:08', '2025-11-10 19:06:00', '2025-11-10 19:06:08');
INSERT INTO `ai_analysis_task` VALUES (41, 26, 2, 'COMPLETED', 'ADMIN', 3, 0, 3, NULL, '2025-11-10 19:08:20', '2025-11-10 19:08:25', '2025-11-10 19:08:20', '2025-11-10 19:08:25');
INSERT INTO `ai_analysis_task` VALUES (42, 28, 4, 'COMPLETED', 'AUTO', 2, 0, 3, NULL, '2025-11-11 09:34:41', '2025-11-11 09:34:49', '2025-11-11 09:34:41', '2025-11-11 09:34:49');
INSERT INTO `ai_analysis_task` VALUES (43, 27, 2, 'COMPLETED', 'AUTO', 2, 0, 3, NULL, '2025-11-11 10:08:14', '2025-11-11 10:08:34', '2025-11-11 10:08:14', '2025-11-11 10:08:34');
INSERT INTO `ai_analysis_task` VALUES (44, 27, 2, 'COMPLETED', 'AUTO', 2, 0, 3, NULL, '2025-11-11 10:10:28', '2025-11-11 10:10:35', '2025-11-11 10:10:28', '2025-11-11 10:10:35');
INSERT INTO `ai_analysis_task` VALUES (45, 27, 2, 'COMPLETED', 'AUTO', 2, 0, 3, NULL, '2025-11-11 10:12:13', '2025-11-11 10:12:18', '2025-11-11 10:12:13', '2025-11-11 10:12:18');

-- ----------------------------
-- Table structure for consultation_message
-- ----------------------------
DROP TABLE IF EXISTS `consultation_message`;
CREATE TABLE `consultation_message`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `session_id` bigint NOT NULL COMMENT '会话ID',
  `sender_type` tinyint NOT NULL COMMENT '发送者类型 1:用户 2:AI助手',
  `message_type` tinyint NULL DEFAULT 1 COMMENT '消息类型 1:文本',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '消息内容',
  `emotion_tag` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '情绪标签',
  `ai_model` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '使用的AI模型',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_session_message`(`session_id` ASC, `created_at` ASC) USING BTREE,
  CONSTRAINT `consultation_message_ibfk_1` FOREIGN KEY (`session_id`) REFERENCES `consultation_session` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '咨询消息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of consultation_message
-- ----------------------------
INSERT INTO `consultation_message` VALUES (98, 17, 1, 1, '你好，我叫小明', NULL, NULL, '2025-09-14 14:11:53');
INSERT INTO `consultation_message` VALUES (99, 17, 2, 1, '你好呀小明！😊 很高兴认识你～我是你的心理健康助手，可以叫我\"暖心\"或者\"小暖\"都可以。最近过得怎么样呀？作为大学生，是不是有很多想分享的事情呢？💬\n\n有什么想聊的都可以跟我说说，无论是学习压力、人际关系，还是对未来的一些迷茫和困惑...我都在这里认真听着呢✨', NULL, 'openai', '2025-09-14 14:11:58');
INSERT INTO `consultation_message` VALUES (100, 17, 1, 1, '我考上研究生了，但是很迷茫', NULL, NULL, '2025-09-14 14:12:09');
INSERT INTO `consultation_message` VALUES (101, 17, 2, 1, '恭喜你考上研究生呀小明！🎉 这真是个值得骄傲的好消息呢～不过听到你说感到迷茫，这种心情我完全能理解😊\n\n读研确实和本科阶段很不一样呢。可以跟我多说说你的迷茫具体来自哪里吗？✨比如是：\n1️⃣ 担心学术压力太大？\n2️⃣ 对研究方向不确定？\n3️⃣ 还是对未来的职业规划有些困惑？\n\n你知道吗？这种\"上岸后的迷茫\"其实很常见哦～就像站在新的起跑线上，眼前有太多可能性时的正常反应🌟\n\n要不要我们一起来梳理看看？慢慢说，我都在听💛', NULL, 'openai', '2025-09-14 14:12:19');
INSERT INTO `consultation_message` VALUES (102, 17, 1, 1, '能给我一些读研建议吗', NULL, NULL, '2025-09-14 14:12:29');
INSERT INTO `consultation_message` VALUES (103, 17, 2, 1, '当然可以呀，小明！🎓 作为即将开启研究生生涯的你，这些建议或许能帮到你呢～✨\n\n🔍 **学术适应篇**：\n1️⃣ 主动联系导师：开学前可以给导师发封邮件，聊聊研究方向和学习计划哦\n2️⃣ 培养文献管理习惯：推荐试试Zotero/EndNote，从第一天就建立文献库\n3️⃣ 每周做学术笔记：记录灵感比事后回忆容易多啦\n\n💡 **心态调整贴士**：\n- 第一学期允许自己\"慢热\"⏳ 很多人需要3-6个月适应节奏\n- 把大目标拆解成季度/月度小目标，成就感UP↑\n- 实验室同门是最佳战友，多分享困惑和资源呀\n\n👩💻 **实用小工具**：\n✓ 番茄工作法APP（专注25分钟+5分钟休息）\n✓ 学术日历标记重要节点\n✓ 定期备份研究数据！（血泪教训预警）\n\n小明注意到你之前提到迷茫，特别想告诉你：研究生阶段迷茫真的太正常了~🤗 建议可以：\n- 9月开学后多参加实验室组会\n- 旁听1-2门相关领域的课\n- 和师兄师姐约咖啡聊聊经验\n\n记得哦，读研是场马拉松不是短跑🏃，找到自己的节奏最重要啦！你现在最想先从哪个方面开始准备呢？💭', NULL, 'openai', '2025-09-14 14:12:47');
INSERT INTO `consultation_message` VALUES (104, 18, 1, 1, '头有点疼', NULL, NULL, '2025-11-10 12:57:39');
INSERT INTO `consultation_message` VALUES (105, 19, 1, 1, '头疼', NULL, NULL, '2025-11-10 13:07:18');
INSERT INTO `consultation_message` VALUES (106, 19, 2, 1, '听到你说头疼，我能感受到你现在一定不太舒服呢。😔 大学生活确实容易因为学习压力、作息不规律等原因引起头痛。我们先深呼吸几次好吗？🌬️\n\n你可以试着：\n1. 找一个安静的地方闭眼休息5分钟\n2. 轻轻按摩太阳穴\n3. 喝些温水\n4. 如果是因为长时间看电脑，可以做做眼保健操\n\n要记住，你的身体是最重要的～如果持续不适的话，建议去校医院看看。需要我陪你聊聊是什么原因引起的头痛吗？', NULL, 'openai', '2025-11-10 13:07:25');
INSERT INTO `consultation_message` VALUES (107, 20, 1, 1, '你好，心情有点不好', NULL, NULL, '2025-11-10 19:05:17');
INSERT INTO `consultation_message` VALUES (108, 20, 2, 1, '亲爱的同学，我能感受到你现在的心情不太好。🥺 这种低落的感觉一定让你很不舒服吧？可以跟我聊聊是什么让你感到不开心吗？无论是学习压力、人际关系，还是其他烦恼，我都会认真倾听。\n\n记住，每个人都会有情绪低落的时候，这很正常。你愿意主动说出来，已经是很好的第一步了呢。💪 \n\n（如果你暂时不想说具体原因也没关系，我们可以先聊聊其他让你感觉好一点的事情哦）', NULL, 'openai', '2025-11-10 19:05:21');
INSERT INTO `consultation_message` VALUES (109, 21, 1, 1, '我想出去旅游', NULL, NULL, '2025-11-12 08:34:20');
INSERT INTO `consultation_message` VALUES (110, 21, 2, 1, '听到你说想出去旅游，我能感受到你对新鲜体验和放松心情的渴望呢～✨  \n\n旅游确实是给自己充电的好方式！不过你现在是遇到什么特别的原因突然想出发吗？比如最近压力有点大，还是单纯想换个环境？🧐  \n\n如果是想调节心情，其实哪怕在学校周边走走、探索些没去过的小店，也能带来新鲜感哦。当然啦，如果条件允许，做好攻略和预算去更远的地方也很棒！需要聊聊具体想去哪里吗？😊  \n\nP.S. 说走就走的旅行听起来很浪漫，但别忘了提前查好天气和交通信息呀～', NULL, 'openai', '2025-11-12 08:34:27');

-- ----------------------------
-- Table structure for consultation_session
-- ----------------------------
DROP TABLE IF EXISTS `consultation_session`;
CREATE TABLE `consultation_session`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `session_title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '会话标题',
  `started_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `last_emotion_analysis` json NULL COMMENT '最后一次情绪分析结果(JSON格式)',
  `last_emotion_updated_at` datetime NULL DEFAULT NULL COMMENT '最后一次情绪分析更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_session`(`user_id` ASC, `started_at` ASC) USING BTREE,
  INDEX `idx_last_emotion_updated_at`(`last_emotion_updated_at` ASC) USING BTREE,
  CONSTRAINT `consultation_session_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '咨询会话表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of consultation_session
-- ----------------------------
INSERT INTO `consultation_session` VALUES (17, 2, '小暖助手 - 9/14/2025, 2:11:52 PM', '2025-09-14 14:11:53', '{\"icon\": \"🤔\", \"label\": \"求知\", \"keywords\": [\"建议\", \"求知\", \"准备\", \"规划\", \"咨询\"], \"riskLevel\": 0, \"timestamp\": 1719216000000, \"isNegative\": false, \"suggestion\": \"很高兴能为你提供读研建议\", \"emotionScore\": 45, \"primaryEmotion\": \"思考\", \"riskDescription\": \"情绪稳定\", \"improvementSuggestions\": [\"列出具体问题清单\", \"咨询学长学姐经验\", \"规划研究方向\", \"制定学习计划\"]}', '2025-09-14 14:12:35');
INSERT INTO `consultation_session` VALUES (18, 2, '小暖助手 - 2025/11/10 12:57:39', '2025-11-10 12:57:39', '{\"icon\": \"😐\", \"label\": \"平静\", \"keywords\": [], \"riskLevel\": 0, \"timestamp\": 1762750675283, \"isNegative\": false, \"suggestion\": \"情绪状态平稳，慢慢来就好\", \"emotionScore\": 50, \"primaryEmotion\": \"中性\", \"riskDescription\": \"当前情绪状态稳定，无需特别关注\", \"improvementSuggestions\": [\"保持规律作息\", \"适当运动\", \"与朋友交流\"]}', '2025-11-10 12:57:55');
INSERT INTO `consultation_session` VALUES (19, 2, '小暖助手 - 2025/11/10 13:07:18', '2025-11-10 13:07:18', '{\"icon\": \"😔\", \"label\": \"困扰\", \"keywords\": [\"头疼\", \"不适\", \"烦恼\"], \"riskLevel\": 1, \"timestamp\": 1711430400000, \"isNegative\": true, \"suggestion\": \"头疼时不妨找个安静的地方休息一会儿\", \"emotionScore\": 45, \"primaryEmotion\": \"困扰\", \"riskDescription\": \"需要关注\", \"improvementSuggestions\": [\"喝杯温水\", \"闭目养神\", \"轻柔按摩太阳穴\"]}', '2025-11-10 13:07:25');
INSERT INTO `consultation_session` VALUES (20, 2, '小暖助手 - 2025/11/10 19:05:16', '2025-11-10 19:05:16', '{\"icon\": \"😢\", \"label\": \"低落\", \"keywords\": [\"心情\", \"不好\", \"低落\"], \"riskLevel\": 1, \"timestamp\": 1711861280000, \"isNegative\": true, \"suggestion\": \"抱抱你，不开心的时候可以找朋友聊聊\", \"emotionScore\": 45, \"primaryEmotion\": \"悲伤\", \"riskDescription\": \"需要关注\", \"improvementSuggestions\": [\"深呼吸放松\", \"听首喜欢的歌\", \"写写心情日记\"]}', '2025-11-10 19:05:23');
INSERT INTO `consultation_session` VALUES (21, 2, '小暖助手 - 2025/11/12 08:34:20', '2025-11-12 08:34:20', '{\"icon\": \"😊\", \"label\": \"开心\", \"keywords\": [\"旅游\", \"期待\", \"放松\"], \"riskLevel\": 0, \"timestamp\": 1713866220000, \"isNegative\": false, \"suggestion\": \"祝你旅途愉快，享受美好时光\", \"emotionScore\": 65, \"primaryEmotion\": \"开心\", \"riskDescription\": \"情绪稳定\", \"improvementSuggestions\": [\"规划旅行路线\", \"准备旅行清单\", \"分享旅行想法\"]}', '2025-11-12 08:34:26');

-- ----------------------------
-- Table structure for emotion_diary
-- ----------------------------
DROP TABLE IF EXISTS `emotion_diary`;
CREATE TABLE `emotion_diary`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日记ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `diary_date` date NOT NULL COMMENT '日记日期',
  `mood_score` tinyint NOT NULL COMMENT '情绪评分(1-10)',
  `dominant_emotion` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '主要情绪',
  `emotion_triggers` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '情绪触发因素',
  `diary_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '日记内容',
  `sleep_quality` tinyint NULL DEFAULT NULL COMMENT '睡眠质量(1-5)',
  `stress_level` tinyint NULL DEFAULT NULL COMMENT '压力水平(1-5)',
  `ai_emotion_analysis` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT 'AI情绪分析结果(JSON格式)',
  `ai_analysis_updated_at` datetime NULL DEFAULT NULL COMMENT 'AI分析更新时间',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_date_unique`(`user_id` ASC, `diary_date` ASC) USING BTREE,
  INDEX `idx_user_diary`(`user_id` ASC, `diary_date` ASC) USING BTREE,
  INDEX `idx_ai_analysis_time`(`ai_analysis_updated_at` ASC) USING BTREE,
  CONSTRAINT `emotion_diary_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '情绪日记表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of emotion_diary
-- ----------------------------
INSERT INTO `emotion_diary` VALUES (2, 2, '2025-09-08', 7, '愉快', '完成了一个重要项目', '今天成功完成了一个重要的项目，感觉很有成就感。团队合作也很顺利，大家都很配合。', 4, 2, NULL, NULL, '2025-09-08 09:41:02', '2025-09-08 09:41:02');
INSERT INTO `emotion_diary` VALUES (3, 2, '2025-09-09', 6, '平静', '日常学习', '今天是比较平常的一天，按计划完成了学习任务。没有特别的事情发生，心情比较平静。', 4, 2, NULL, NULL, '2025-09-09 09:41:02', '2025-09-09 09:41:02');
INSERT INTO `emotion_diary` VALUES (4, 2, '2025-09-10', 8, '兴奋', '收到好消息', '收到了期待已久的好消息！心情特别好，忍不住想要分享给身边的朋友。', 5, 1, NULL, NULL, '2025-09-10 09:41:02', '2025-09-10 09:41:02');
INSERT INTO `emotion_diary` VALUES (5, 2, '2025-09-11', 4, '焦虑', '考试压力', '明天有一场重要的考试，复习得不够充分，感到很焦虑。担心会考不好。', 3, 4, '{\"primaryEmotion\":\"焦虑\",\"emotionScore\":40,\"isNegative\":true,\"riskLevel\":1,\"keywords\":[\"考试\",\"压力\",\"焦虑\",\"复习\"],\"suggestion\":\"适当的压力可以转化为动力\",\"icon\":\"😰\",\"label\":\"焦虑\",\"riskDescription\":\"需要关注\",\"improvementSuggestions\":[\"深呼吸放松\",\"做最后一次重点复习\",\"保证充足睡眠\"],\"timestamp\":1716192000000}', '2025-09-14 14:05:52', '2025-09-11 09:41:02', '2025-09-14 14:05:51');
INSERT INTO `emotion_diary` VALUES (6, 2, '2025-09-12', 6, '轻松', '考试结束', '考试终于结束了，虽然不知道结果如何，但至少压力释放了。可以好好休息一下。', 4, 2, NULL, NULL, '2025-09-12 09:41:02', '2025-09-12 09:41:02');
INSERT INTO `emotion_diary` VALUES (7, 2, '2025-09-13', 5, '疲惫', '工作繁忙', '今天工作特别忙，一直在处理各种事务。感觉身心都有些疲惫，需要好好休息。', 3, 3, NULL, NULL, '2025-09-13 09:41:02', '2025-09-13 09:41:02');
INSERT INTO `emotion_diary` VALUES (8, 2, '2025-09-14', 3, '焦虑', '和朋友闹矛盾', '和朋友闹矛盾，很难过', 2, 4, '{\"primaryEmotion\":\"焦虑\",\"emotionScore\":75,\"isNegative\":true,\"riskLevel\":2,\"keywords\":[\"朋友\",\"矛盾\",\"焦虑\",\"压力\",\"睡眠\"],\"suggestion\":\"和朋友之间的矛盾让你焦虑，试着冷静下来沟通\",\"icon\":\"😰\",\"label\":\"焦虑\",\"riskDescription\":\"需要心理疏导\",\"improvementSuggestions\":[\"深呼吸放松心情\",\"和朋友坦诚沟通\",\"改善睡眠环境\",\"适当运动缓解压力\"],\"timestamp\":1719216000000}', '2025-09-14 14:14:45', '2025-09-14 09:41:02', '2025-09-14 14:14:44');
INSERT INTO `emotion_diary` VALUES (14, 4, '2025-09-08', 5, '紧张', '新环境适应', '刚到新环境，对一切都感到陌生。需要时间去适应新的生活节奏。', 3, 3, NULL, NULL, '2025-09-08 09:41:02', '2025-09-08 09:41:02');
INSERT INTO `emotion_diary` VALUES (15, 4, '2025-09-09', 6, '好奇', '探索新事物', '开始探索周围的环境，发现了很多有趣的地方。对未来充满好奇。', 4, 2, NULL, NULL, '2025-09-09 09:41:02', '2025-09-09 09:41:02');
INSERT INTO `emotion_diary` VALUES (16, 4, '2025-09-10', 8, '兴奋', '结识新朋友', '今天认识了几个很有趣的新朋友，大家聊得很投机。感觉生活开始变得丰富多彩。', 5, 1, NULL, NULL, '2025-09-10 09:41:02', '2025-09-10 09:41:02');
INSERT INTO `emotion_diary` VALUES (17, 4, '2025-09-11', 4, '孤独', '思念家乡', '突然很想念家乡和家人，感到有些孤独。虽然新环境很好，但还是会想家。', 3, 3, NULL, NULL, '2025-09-11 09:41:02', '2025-09-11 09:41:02');
INSERT INTO `emotion_diary` VALUES (18, 4, '2025-09-12', 7, '充实', '忙碌的一天', '今天过得很充实，完成了很多事情。感觉自己在慢慢适应新的生活。', 4, 2, NULL, NULL, '2025-09-12 09:41:02', '2025-09-12 09:41:02');
INSERT INTO `emotion_diary` VALUES (19, 4, '2025-09-13', 3, '挫折', '遇到困难', '遇到了一些困难，感到有些挫败。但我相信通过努力一定能够克服。', 2, 4, NULL, NULL, '2025-09-13 09:41:02', '2025-09-13 09:41:02');
INSERT INTO `emotion_diary` VALUES (20, 4, '2025-09-14', 6, '坚定', '制定计划', '为自己制定了详细的计划，感觉目标更加清晰了。有了方向就有了动力。', 4, 2, NULL, NULL, '2025-09-14 09:41:02', '2025-09-14 09:41:02');
INSERT INTO `emotion_diary` VALUES (26, 2, '2025-11-10', 10, '开心', '开心', '愉快', 5, 1, '{\"primaryEmotion\":\"开心\",\"emotionScore\":100,\"isNegative\":false,\"riskLevel\":0,\"keywords\":[\"愉快\",\"开心\",\"满足\"],\"suggestion\":\"保持这份美好心情，享受当下的快乐\",\"icon\":\"😊\",\"label\":\"愉快\",\"riskDescription\":\"情绪稳定\",\"improvementSuggestions\":[\"分享快乐时刻\",\"记录美好心情\",\"做些喜欢的事\"],\"timestamp\":1711861280000}', '2025-11-10 19:08:25', '2025-11-10 13:00:26', '2025-11-10 19:08:24');
INSERT INTO `emotion_diary` VALUES (27, 2, '2025-11-11', 10, '开心', '开心', '开心', 5, 1, '{\"primaryEmotion\":\"开心\",\"emotionScore\":100,\"isNegative\":false,\"riskLevel\":0,\"keywords\":[\"开心\",\"兴奋\",\"满足\"],\"suggestion\":\"继续保持这份美好的心情吧\",\"icon\":\"😄\",\"label\":\"开心\",\"riskDescription\":\"情绪极佳\",\"improvementSuggestions\":[\"分享你的快乐\",\"记录美好时刻\",\"做些有趣的事\"],\"timestamp\":1715000000000}', '2025-11-11 10:12:18', '2025-11-11 08:44:08', '2025-11-11 10:12:17');
INSERT INTO `emotion_diary` VALUES (28, 4, '2025-11-11', 6, '平静', '开心', '开心', 4, 2, '{\"primaryEmotion\":\"开心\",\"emotionScore\":60,\"isNegative\":false,\"riskLevel\":0,\"keywords\":[\"开心\",\"平静\",\"满足\"],\"suggestion\":\"保持这份开心的心情吧\",\"icon\":\"😊\",\"label\":\"开心\",\"riskDescription\":\"情绪稳定\",\"improvementSuggestions\":[\"记录美好时刻\",\"分享快乐给朋友\",\"做点喜欢的事\"],\"timestamp\":1715000000000}', '2025-11-11 09:34:49', '2025-11-11 09:34:07', '2025-11-11 09:34:48');

-- ----------------------------
-- Table structure for knowledge_article
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_article`;
CREATE TABLE `knowledge_article`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章ID(UUID)',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章标题',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '文章摘要',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章内容',
  `cover_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面图片',
  `tags` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标签',
  `author_id` bigint NULL DEFAULT NULL COMMENT '作者ID',
  `read_count` int NULL DEFAULT 0 COMMENT '阅读次数',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 1:已发布',
  `published_at` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `author_id`(`author_id` ASC) USING BTREE,
  INDEX `idx_category_article`(`category_id` ASC, `published_at` ASC) USING BTREE,
  INDEX `idx_title`(`title` ASC) USING BTREE,
  CONSTRAINT `knowledge_article_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `knowledge_category` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `knowledge_article_ibfk_2` FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '知识文章表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of knowledge_article
-- ----------------------------
INSERT INTO `knowledge_article` VALUES ('550e8400-e29b-41d4-a716-446655440001', 1, '如何识别和管理焦虑情绪', '焦虑是现代生活中常见的情绪反应，学会识别和管理焦虑对心理健康至关重要。', '<p>焦虑是一种正常的情绪反应，但过度的焦虑可能会影响我们的日常生活。本文将介绍如何识别焦虑的症状，以及一些有效的管理方法。</p><h3>识别焦虑的症状</h3><ul><li>心跳加速、出汗</li><li>呼吸急促</li><li>思维混乱</li><li>难以集中注意力</li></ul><h3>管理焦虑的方法</h3><ol><li>深呼吸练习</li><li>正念冥想</li><li>适度运动</li><li>寻求专业帮助</li></ol>', '', '焦虑,情绪管理,心理健康', 1, 15, 1, '2025-09-01 10:00:00', '2025-09-01 10:00:00', '2025-09-01 10:00:00');
INSERT INTO `knowledge_article` VALUES ('550e8400-e29b-41d4-a716-446655440002', 2, '情绪调节的五个有效策略', '掌握情绪调节技巧，让生活更加平衡和谐。', '<p>情绪调节是心理健康的重要组成部分。以下是五个经过科学验证的情绪调节策略：</p><h3>1. 认知重构</h3><p>学会从不同角度看待问题，挑战负面思维模式。</p><h3>2. 情绪标记</h3><p>准确识别和命名自己的情绪状态。</p><h3>3. 渐进式肌肉放松</h3><p>通过身体放松来缓解情绪紧张。</p><h3>4. 表达性写作</h3><p>通过写作来处理复杂的情绪体验。</p><h3>5. 社交支持</h3><p>寻求朋友和家人的理解与支持。</p>', '', '情绪调节,心理技巧,自我管理', 1, 23, 1, '2025-09-02 14:30:00', '2025-09-02 14:30:00', '2025-09-02 14:30:00');
INSERT INTO `knowledge_article` VALUES ('550e8400-e29b-41d4-a716-446655440003', 3, '职场压力管理指南', '在快节奏的工作环境中，学会有效管理压力是职业成功的关键。', '<p>职场压力是现代工作生活中不可避免的挑战。本指南将帮助您建立有效的压力管理体系。</p><h3>识别压力源</h3><ul><li>工作量过大</li><li>人际关系紧张</li><li>职业发展焦虑</li><li>工作与生活平衡困难</li></ul><h3>压力管理策略</h3><ol><li>时间管理技巧</li><li>设定合理目标</li><li>建立支持网络</li><li>培养兴趣爱好</li><li>定期休息和放松</li></ol><h3>长期压力预防</h3><p>建立健康的工作习惯和生活方式，预防压力累积。</p>', '', '职场压力,时间管理,工作生活平衡', 1, 31, 1, '2025-09-03 09:15:00', '2025-09-03 09:15:00', '2025-09-03 09:15:00');
INSERT INTO `knowledge_article` VALUES ('550e8400-e29b-41d4-a716-446655440004', 4, '建立健康的人际关系', '良好的人际关系是心理健康和生活幸福的基石。', '<p>人际关系质量直接影响我们的心理健康和生活满意度。本文将探讨如何建立和维护健康的人际关系。</p><h3>健康关系的特征</h3><ul><li>相互尊重和信任</li><li>有效沟通</li><li>情感支持</li><li>个人边界清晰</li></ul><h3>沟通技巧</h3><ol><li>积极倾听</li><li>表达真实感受</li><li>非暴力沟通</li><li>冲突解决</li></ol><h3>维护关系的方法</h3><p>定期联系、表达感激、共同成长、处理分歧。</p>', '', '人际关系,沟通技巧,社交能力', 1, 18, 1, '2025-09-04 16:20:00', '2025-09-04 16:20:00', '2025-09-04 16:20:00');
INSERT INTO `knowledge_article` VALUES ('550e8400-e29b-41d4-a716-446655440005', 1, '睡眠质量与心理健康', '优质睡眠是维护心理健康的重要基础。', '<p>睡眠质量与心理健康密切相关。充足的睡眠不仅能恢复身体机能，还对情绪调节和认知功能有重要影响。</p><h3>睡眠对心理健康的影响</h3><ul><li>情绪稳定性</li><li>压力应对能力</li><li>记忆和学习能力</li><li>注意力集中</li></ul><h3>改善睡眠质量的方法</h3><ol><li>建立规律作息</li><li>创造良好睡眠环境</li><li>睡前放松活动</li><li>避免刺激性物质</li><li>适度运动</li></ol>', '', '睡眠健康,心理健康,生活习惯', 1, 27, 1, '2025-09-05 11:45:00', '2025-09-05 11:45:00', '2025-09-05 11:45:00');
INSERT INTO `knowledge_article` VALUES ('550e8400-e29b-41d4-a716-446655440006', 2, '正念练习入门指南', '正念练习是现代心理学推荐的有效减压和情绪调节方法。', '<p>正念是一种专注于当下体验的练习方法，能够帮助我们更好地管理情绪和压力。</p><h3>什么是正念</h3><p>正念是有意识地、不带评判地关注当下时刻的能力。</p><h3>正念的益处</h3><ul><li>减少焦虑和抑郁</li><li>提高专注力</li><li>改善情绪调节</li><li>增强自我觉察</li></ul><h3>基础正念练习</h3><ol><li>呼吸观察</li><li>身体扫描</li><li>行走冥想</li><li>日常正念</li></ol><h3>练习建议</h3><p>从每天5-10分钟开始，逐渐增加练习时间。</p>', '', '正念,冥想,减压,专注力', 1, 42, 1, '2025-09-06 13:10:00', '2025-09-06 13:10:00', '2025-09-06 13:10:00');
INSERT INTO `knowledge_article` VALUES ('550e8400-e29b-41d4-a716-446655440007', 3, '学生心理压力应对策略', '帮助学生识别和应对学习生活中的各种心理压力。', '<p>学生时期面临着学业、人际、未来规划等多重压力。掌握有效的应对策略对学生心理健康至关重要。</p><h3>常见压力源</h3><ul><li>学业成绩压力</li><li>同伴关系</li><li>家庭期望</li><li>未来规划焦虑</li></ul><h3>应对策略</h3><ol><li>合理规划学习时间</li><li>培养兴趣爱好</li><li>建立支持网络</li><li>学会放松技巧</li><li>寻求专业帮助</li></ol><h3>预防措施</h3><p>建立健康的学习习惯和生活方式。</p>', '', '学生心理,学业压力,青少年心理', 1, 35, 1, '2025-09-07 08:30:00', '2025-09-07 08:30:00', '2025-11-10 19:07:53');

-- ----------------------------
-- Table structure for knowledge_category
-- ----------------------------
DROP TABLE IF EXISTS `knowledge_category`;
CREATE TABLE `knowledge_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父分类ID',
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分类名称',
  `category_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '分类代码',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '分类描述',
  `sort_order` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0:禁用 1:启用',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `category_code`(`category_code` ASC) USING BTREE,
  INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE,
  INDEX `idx_sort_order`(`sort_order` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '知识文章分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of knowledge_category
-- ----------------------------
INSERT INTO `knowledge_category` VALUES (1, 0, '心理健康基础', NULL, '心理健康基础知识和概念.', 30, 1, '2025-09-04 12:50:03', '2025-09-13 11:48:16');
INSERT INTO `knowledge_category` VALUES (2, 0, '情绪管理', NULL, '情绪识别、调节和管理技巧', 40, 1, '2025-09-04 12:50:03', '2025-09-13 11:48:16');
INSERT INTO `knowledge_category` VALUES (3, 0, '压力缓解', NULL, '压力来源分析和缓解方法', 20, 1, '2025-09-04 12:50:03', '2025-09-13 11:48:16');
INSERT INTO `knowledge_category` VALUES (4, 0, '人际关系', NULL, '人际交往和关系处理', 10, 1, '2025-09-04 12:50:03', '2025-09-13 11:48:16');

-- ----------------------------
-- Table structure for sys_file_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_info`;
CREATE TABLE `sys_file_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文件ID，主键自增',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '原始文件名（用户上传时的文件名）',
  `file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件访问路径（服务器存储路径）',
  `file_size` bigint NOT NULL DEFAULT 0 COMMENT '文件大小，单位：字节',
  `file_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件类型（IMG/PDF/TXT/DOC/XLS等）',
  `business_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '业务类型（用于区分文件用途，如：avatar/document/attachment）',
  `business_id` char(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '业务对象ID（关联的业务数据主键）',
  `business_field` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '业务字段名（对应业务表中的字段名）',
  `upload_user_id` bigint NULL DEFAULT NULL COMMENT '上传用户ID（记录谁上传的文件）',
  `is_temp` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否临时文件（0:否 1:是）',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态（0:删除 1:正常）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间（仅对临时文件有效）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_business`(`business_type` ASC, `business_id` ASC) USING BTREE,
  INDEX `idx_upload_user`(`upload_user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_temp_expire`(`is_temp` ASC, `expire_time` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统文件信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_file_info
-- ----------------------------
INSERT INTO `sys_file_info` VALUES (46, '7487e74da9ec1ee07b5064348942b2ca.png', '/files/bussiness/article/1756963989972.png', 178205, 'IMG', 'ARTICLE', 'b51ecba7-db3c-4a30-84ab-3a19e1e8264b', 'cover', 1, 0, 1, '2025-09-04 13:33:10', NULL);
INSERT INTO `sys_file_info` VALUES (47, '7487e74da9ec1ee07b5064348942b2ca.png', '/files/bussiness/article/1756964062385.png', 178205, 'IMG', 'ARTICLE', 'null', 'cover', 1, 0, 1, '2025-09-04 13:34:22', NULL);
INSERT INTO `sys_file_info` VALUES (48, '7487e74da9ec1ee07b5064348942b2ca.png', '/files/bussiness/article/1756964101109.png', 178205, 'IMG', 'ARTICLE', 'e26ad80e-87b2-490f-87c3-2d5f981ba7bf', 'cover', 1, 0, 1, '2025-09-04 13:35:01', NULL);
INSERT INTO `sys_file_info` VALUES (49, '7487e74da9ec1ee07b5064348942b2ca.png', '/files/bussiness/article/1756964116665.png', 178205, 'IMG', 'ARTICLE', '8d79085d-4686-4af7-813d-4f97c936ff3e', 'cover', 1, 0, 1, '2025-09-04 13:35:17', NULL);
INSERT INTO `sys_file_info` VALUES (50, 'image323232s.jpg', '/files/temp/1757670438125.jpg', 7727, 'IMG', 'USER_AVATAR', '1', 'avatar', 1, 0, 0, '2025-09-12 17:47:18', '2025-09-13 17:47:18');
INSERT INTO `sys_file_info` VALUES (51, 'Untitled.jpg', '/files/temp/1757670832161.jpg', 7393, 'IMG', 'USER_AVATAR', '1', 'avatar', 1, 0, 0, '2025-09-12 17:53:52', '2025-09-13 17:53:52');
INSERT INTO `sys_file_info` VALUES (52, 'ima9897ges.jpg', '/files/temp/1757670856780.jpg', 4977, 'IMG', 'USER_AVATAR', '1', 'avatar', 1, 0, 0, '2025-09-12 17:54:17', '2025-09-13 17:54:17');
INSERT INTO `sys_file_info` VALUES (53, 'image323232s.jpg', '/files/bussiness/user_avatar/1757687320361.jpg', 7727, 'IMG', 'USER_AVATAR', '1', 'avatar', 1, 0, 1, '2025-09-12 22:28:40', NULL);
INSERT INTO `sys_file_info` VALUES (54, '0a157ddd8a7070c5240fb3ad23a5fc38.jpeg', '/files/bussiness/article/1757687894684.jpeg', 135321, 'IMG', 'ARTICLE', '550e8400-e29b-41d4-a716-446655440008', 'cover', 1, 0, 1, '2025-09-12 22:38:15', NULL);
INSERT INTO `sys_file_info` VALUES (55, 'Unti555tled.jpg', '/files/bussiness/user_avatar/1757816752928.jpg', 6389, 'IMG', 'USER_AVATAR', '2', 'avatar', 2, 0, 1, '2025-09-14 10:25:53', NULL);
INSERT INTO `sys_file_info` VALUES (56, '8b3e121c9f4bb2cfc3603a6ad576da8f.jpg', '/files/bussiness/article/1762908097203.jpg', 75721, 'IMG', 'ARTICLE', '550e8400-e29b-41d4-a716-446655440007', 'cover', 1, 0, 1, '2025-11-12 08:41:37', NULL);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像URL',
  `gender` tinyint NULL DEFAULT NULL COMMENT '性别 0:未知 1:男 2:女',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `user_type` tinyint NULL DEFAULT 1 COMMENT '用户类型 1:普通用户 2:管理员',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态 0:禁用 1:正常',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE,
  UNIQUE INDEX `phone`(`phone` ASC) USING BTREE,
  INDEX `idx_username`(`username` ASC) USING BTREE,
  INDEX `idx_user_type`(`user_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'admin@example.com', '13123456789', '$2a$10$qUjCfQLqf9qVt1w1LDoaj.f5TUzBYcXh3FNNA0BhoQ54Vv2cSUt7K', '系统管理员', '/files/bussiness/user_avatar/1757687320361.jpg', 1, '2019-09-02', 2, 1, '2025-08-30 12:00:01', '2025-09-12 22:28:42');
INSERT INTO `user` VALUES (2, 'test', '15165@qq.com', '13132584165', '$2a$10$qUjCfQLqf9qVt1w1LDoaj.f5TUzBYcXh3FNNA0BhoQ54Vv2cSUt7K', 'tyest', '/files/bussiness/user_avatar/1757816752928.jpg', 1, '2002-08-01', 1, 1, '2025-08-30 17:22:24', '2025-09-14 10:25:54');
INSERT INTO `user` VALUES (4, 'ces', '111111111111@qq.com', '13123456654', '$2a$10$Sy22CXQJe5OAS.Xvmbb60ul9O4a5qi5FtnOjFC2v/Ruk2binJ7vEm', '1561', NULL, 1, '2025-09-10', 1, 1, '2025-08-30 18:53:07', '2025-11-10 19:07:27');

-- ----------------------------
-- Table structure for user_favorite
-- ----------------------------
DROP TABLE IF EXISTS `user_favorite`;
CREATE TABLE `user_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `article_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章ID',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_article_unique`(`user_id` ASC, `article_id` ASC) USING BTREE,
  INDEX `article_id`(`article_id` ASC) USING BTREE,
  CONSTRAINT `user_favorite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `user_favorite_ibfk_2` FOREIGN KEY (`article_id`) REFERENCES `knowledge_article` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_favorite
-- ----------------------------
INSERT INTO `user_favorite` VALUES (8, 2, '550e8400-e29b-41d4-a716-446655440007', '2025-11-10 19:06:27');

SET FOREIGN_KEY_CHECKS = 1;
