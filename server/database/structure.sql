-- phpMyAdmin SQL Dump
-- version 2.11.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 10, 2016 at 03:29 PM
-- Server version: 5.5.17
-- PHP Version: 5.2.14p3

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `mimishop1`
--

-- --------------------------------------------------------

--
-- Table structure for table `tb_access`
--

CREATE TABLE IF NOT EXISTS `tb_access` (
  `cnt` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tb_ads`
--

CREATE TABLE IF NOT EXISTS `tb_ads` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `adsImgID` int(11) NOT NULL,
  `adsURL` varchar(255) NOT NULL DEFAULT '',
  `adsStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `ads_ImageID` (`adsImgID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=66 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_agreement`
--

CREATE TABLE IF NOT EXISTS `tb_agreement` (
  `type` tinyint(4) NOT NULL,
  `content` text NOT NULL,
  `modify_time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `tb_app`
--

CREATE TABLE IF NOT EXISTS `tb_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appMarketID` int(8) NOT NULL DEFAULT '1',
  `appVersion` varchar(8) NOT NULL,
  `appNoticeType` varchar(8) NOT NULL DEFAULT '',
  `appOS` varchar(16) NOT NULL DEFAULT 'A',
  `appStatus` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `app_marketid` (`appMarketID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_banner`
--

CREATE TABLE IF NOT EXISTS `tb_banner` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bannerAdsID` int(11) NOT NULL,
  `bannerTitle` varchar(255) NOT NULL DEFAULT '',
  `bannerStartDate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `bannerEndDate` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `bannerContent` varchar(1500) NOT NULL DEFAULT '',
  `bannerType` int(11) NOT NULL DEFAULT '0',
  `bannerShowMode` int(1) NOT NULL DEFAULT '0',
  `bannerBackImgID` int(11) DEFAULT NULL,
  `bannerDateShow` tinyint(1) NOT NULL DEFAULT '1',
  `bannerTitleShow` tinyint(1) NOT NULL DEFAULT '1',
  `bannerContentShow` tinyint(1) NOT NULL DEFAULT '1',
  `bannerStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `banner_AdsID` (`bannerAdsID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_bannerclick`
--

CREATE TABLE IF NOT EXISTS `tb_bannerclick` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bannerclickBannerID` int(11) NOT NULL,
  `bannerclickTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `bannerclickStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `bannerclick_BannerID` (`bannerclickBannerID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=542 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_bannercomment`
--

CREATE TABLE IF NOT EXISTS `tb_bannercomment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bannercommentPostManID` int(11) NOT NULL,
  `bannercommentBannerID` int(11) NOT NULL,
  `bannercommentContent` varchar(1000) NOT NULL DEFAULT '',
  `bannercommentPostTime` bigint(36) NOT NULL DEFAULT '0',
  `bannercommentHeartCnt` int(11) NOT NULL DEFAULT '0',
  `bannercommentStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `castcomment_PostManID` (`bannercommentPostManID`),
  KEY `castcomment_CastID` (`bannercommentBannerID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=67 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_cast`
--

CREATE TABLE IF NOT EXISTS `tb_cast` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `castTitle` varchar(255) NOT NULL DEFAULT '',
  `castCoveredImgID` int(11) DEFAULT NULL,
  `castCategoryID` int(11) NOT NULL DEFAULT '1',
  `castPostManID` int(11) DEFAULT NULL,
  `castPostTime` datetime NOT NULL DEFAULT '2015-09-01 00:00:00',
  `castShareCnt` int(11) NOT NULL DEFAULT '0',
  `castHeartCnt` int(11) NOT NULL DEFAULT '0',
  `castBest` varchar(1) NOT NULL DEFAULT 'N',
  `castConsequence` int(11) NOT NULL DEFAULT '0',
  `castStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `cast_CategoryID` (`castCategoryID`),
  KEY `cast_CoveredImgID` (`castCoveredImgID`),
  KEY `cast_PostManID` (`castPostManID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=24 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_castcomment`
--

CREATE TABLE IF NOT EXISTS `tb_castcomment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `castcommentPostManID` int(11) NOT NULL,
  `castcommentCastID` int(11) NOT NULL,
  `castcommentContent` varchar(1000) NOT NULL,
  `castcommentPostTime` bigint(36) NOT NULL,
  `castcommentHeartCnt` int(11) NOT NULL DEFAULT '0',
  `castcommentStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `castcomment_PostManID` (`castcommentPostManID`),
  KEY `castcomment_CastID` (`castcommentCastID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=52 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_castcommentrelation`
--

CREATE TABLE IF NOT EXISTS `tb_castcommentrelation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `castcommentrelationCastCommentID` int(11) NOT NULL,
  `castcommentrelationPostManID` int(11) NOT NULL,
  `castcommentrelationLike` tinyint(1) NOT NULL,
  `castcommentrelationStatus` tinyint(1) NOT NULL DEFAULT '0',
  `castcommentrelationPostTime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `castcommentrelation_CastCommentID` (`castcommentrelationCastCommentID`),
  KEY `castcommentrelation_PostManID` (`castcommentrelationPostManID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_castdetail`
--

CREATE TABLE IF NOT EXISTS `tb_castdetail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `castdetailImgID` int(11) DEFAULT NULL,
  `castdetailCastID` int(11) NOT NULL,
  `castdetailContent` varchar(1000) NOT NULL,
  `castdetailIdx` int(11) NOT NULL DEFAULT '0',
  `castdetailPublish` varchar(1000) NOT NULL DEFAULT '',
  `castdetailStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `castdetail_ImgID` (`castdetailImgID`),
  KEY `castdetail_CastID` (`castdetailCastID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=262 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_castrelation`
--

CREATE TABLE IF NOT EXISTS `tb_castrelation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `castrelationCastID` int(11) NOT NULL,
  `castrelationPostManID` int(11) NOT NULL,
  `castrelationLike` tinyint(1) NOT NULL DEFAULT '0',
  `castrelationShare` tinyint(1) NOT NULL DEFAULT '0',
  `castrelationStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `castrelation_PostManID` (`castrelationPostManID`),
  KEY `castrelation_CastID` (`castrelationCastID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_category`
--

CREATE TABLE IF NOT EXISTS `tb_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `categoryName` varchar(255) NOT NULL,
  `categoryShop` tinyint(1) NOT NULL DEFAULT '0',
  `categoryFreetalk` tinyint(1) NOT NULL DEFAULT '1',
  `categoryCast` tinyint(1) NOT NULL DEFAULT '0',
  `categoryEvent` tinyint(1) NOT NULL DEFAULT '0',
  `categoryStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=19 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_event`
--

CREATE TABLE IF NOT EXISTS `tb_event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eventShopID` int(11) NOT NULL,
  `eventImgID` int(11) DEFAULT NULL,
  `eventSummary` varchar(255) NOT NULL DEFAULT '',
  `eventContent` varchar(1000) NOT NULL DEFAULT '',
  `eventMoney` int(16) NOT NULL DEFAULT '0',
  `eventClickCnt` int(11) NOT NULL DEFAULT '0',
  `eventStart` date NOT NULL,
  `eventEnd` date NOT NULL,
  `eventConsequence` int(11) NOT NULL DEFAULT '99999999',
  `eventStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `event_shopID` (`eventShopID`),
  KEY `event_imgID` (`eventImgID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=53 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_freetalk`
--

CREATE TABLE IF NOT EXISTS `tb_freetalk` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `freetalkPostManID` int(11) NOT NULL,
  `freetalkCategoryID` int(11) NOT NULL,
  `freetalkContent` varchar(1000) NOT NULL DEFAULT '',
  `freetalkHeartCnt` int(11) NOT NULL DEFAULT '0',
  `freetalkReviewCnt` int(11) NOT NULL DEFAULT '0',
  `freetalkPostTime` bigint(36) NOT NULL,
  `freetalkImgIDArrayString` varchar(225) NOT NULL DEFAULT '[]',
  `freetalkBest` varchar(1) NOT NULL DEFAULT 'N',
  `freetalkStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `freetalk_categoryID` (`freetalkCategoryID`),
  KEY `freetalk_imgID` (`freetalkImgIDArrayString`),
  KEY `freetalk_userID` (`freetalkPostManID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=264 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_freetalkcomment`
--

CREATE TABLE IF NOT EXISTS `tb_freetalkcomment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `freetalkcommentContent` varchar(255) NOT NULL DEFAULT '',
  `freetalkcommentPostManID` int(11) NOT NULL,
  `freetalkcommentPostTime` bigint(36) NOT NULL DEFAULT '0',
  `freetalkcommentFreetalkID` int(11) NOT NULL,
  `freetalkcommentHeartCnt` int(11) NOT NULL DEFAULT '0',
  `freetalkcommentRedList` varchar(1000) NOT NULL DEFAULT '[]',
  `freetalkcommentStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `freetalkcomment_PostManID` (`freetalkcommentPostManID`),
  KEY `freetalkcomment_FreetalkID` (`freetalkcommentFreetalkID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=125 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_freetalkcommentrelation`
--

CREATE TABLE IF NOT EXISTS `tb_freetalkcommentrelation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `freetalkcommentrelationFreetalkCommentID` int(11) NOT NULL,
  `freetalkcommentrelationPostManID` int(11) NOT NULL,
  `freetalkcommentrelationLike` tinyint(1) NOT NULL DEFAULT '0',
  `freetalkcommentrelationDeclare` tinyint(1) NOT NULL DEFAULT '0',
  `freetalkcommentrelationPostTime` datetime NOT NULL,
  `freetalkcommentrelationStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `freetalkcommentrelation_FreetalkCommentID` (`freetalkcommentrelationFreetalkCommentID`),
  KEY `freetalkcommentrelation_PostManID` (`freetalkcommentrelationPostManID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_freetalkcommenttag`
--

CREATE TABLE IF NOT EXISTS `tb_freetalkcommenttag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `freetalkcommenttagFreetalkCommentID` int(11) NOT NULL,
  `freetalkcommenttagTAGUserID` int(11) NOT NULL,
  `freetalkcommenttagPostTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `freetalkcommenttagStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `freetalkcommenttag_FreetalkCommentID` (`freetalkcommenttagFreetalkCommentID`),
  KEY `freetalkcommenttag_TAGUserID` (`freetalkcommenttagTAGUserID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=16 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_freetalkrelation`
--

CREATE TABLE IF NOT EXISTS `tb_freetalkrelation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `freetalkrelationFreetalkID` int(11) NOT NULL,
  `freetalkrelationPostManID` int(11) NOT NULL,
  `freetalkrelationDeclare` tinyint(1) NOT NULL DEFAULT '0',
  `freetalkrelationLike` tinyint(1) NOT NULL DEFAULT '0',
  `freetalkrelationPostTime` datetime NOT NULL,
  `freetalkrelationStatus` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `freetalkrelation_FreetalkID` (`freetalkrelationFreetalkID`),
  KEY `freetalkrelation_PostManID` (`freetalkrelationPostManID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=21 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_gcmlog`
--

CREATE TABLE IF NOT EXISTS `tb_gcmlog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gcmlogUserID` int(11) NOT NULL,
  `gcmlogType` varchar(1) NOT NULL DEFAULT '',
  `gcmlogTitle` varchar(255) NOT NULL DEFAULT '',
  `gcmlogContent` varchar(1000) NOT NULL DEFAULT '',
  `gcmlogPostTime` datetime NOT NULL DEFAULT '2015-10-01 00:00:00',
  `gcmlogReadTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `gcmlogReaded` tinyint(1) NOT NULL DEFAULT '0',
  `gcmlogStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `gcmlog_userID` (`gcmlogUserID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=677 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_image`
--

CREATE TABLE IF NOT EXISTS `tb_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `imageURL` varchar(255) NOT NULL DEFAULT '',
  `imageUploadUserID` int(11) NOT NULL DEFAULT '0',
  `imageUploadTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `imageUploadLocation` int(2) NOT NULL DEFAULT '0',
  `imageStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `image_UploadUserID` (`imageUploadUserID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=174177 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_life`
--

CREATE TABLE IF NOT EXISTS `tb_life` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lifeTitle` varchar(255) NOT NULL DEFAULT '',
  `lifeAdsID` int(11) NOT NULL,
  `lifeClickCnt` int(11) NOT NULL DEFAULT '0',
  `lifeCategoryID` int(11) NOT NULL,
  `lifeRegTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lifeSubject` varchar(255) NOT NULL DEFAULT '',
  `lifeExplain` varchar(1000) NOT NULL DEFAULT '',
  `lifeConsequence` int(11) NOT NULL DEFAULT '1',
  `lifeBest` varchar(1) NOT NULL DEFAULT 'N',
  `lifeStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `life_adsID` (`lifeAdsID`),
  KEY `life_categoryID` (`lifeCategoryID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=28 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_lifeclick`
--

CREATE TABLE IF NOT EXISTS `tb_lifeclick` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `lifeclickLifeID` int(11) NOT NULL,
  `lifeclickTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `lifeclickStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `lifeclick_LifeID` (`lifeclickLifeID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=140 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_location`
--

CREATE TABLE IF NOT EXISTS `tb_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `locationName1` varchar(255) NOT NULL DEFAULT '',
  `locationName2` varchar(255) NOT NULL DEFAULT '',
  `locationName3` varchar(255) NOT NULL,
  `locationName4` varchar(255) NOT NULL,
  `locationLongtitude` double(15,10) NOT NULL DEFAULT '0.0000000000',
  `locationLatitude` double(15,10) NOT NULL DEFAULT '0.0000000000',
  `locationStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=24473 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_market`
--

CREATE TABLE IF NOT EXISTS `tb_market` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `marketName` varchar(24) NOT NULL,
  `marketStatus` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=3 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_notice`
--

CREATE TABLE IF NOT EXISTS `tb_notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `noticeTitle` varchar(255) NOT NULL DEFAULT '',
  `noticeContent` varchar(2000) NOT NULL DEFAULT '',
  `noticeClickCnt` int(11) NOT NULL DEFAULT '0',
  `noticeTime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `noticeStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_product`
--

CREATE TABLE IF NOT EXISTS `tb_product` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productName` varchar(255) NOT NULL DEFAULT '',
  `productPrice` varchar(255) NOT NULL DEFAULT '0',
  `productEventPrice` varchar(255) NOT NULL DEFAULT '0',
  `productShopID` int(11) NOT NULL,
  `productStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `product_shopID` (`productShopID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=20981 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_question`
--

CREATE TABLE IF NOT EXISTS `tb_question` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `questionPostManID` int(11) NOT NULL,
  `questionContent` varchar(2000) NOT NULL DEFAULT '',
  `questionPostTime` datetime NOT NULL,
  `questionType` int(1) NOT NULL DEFAULT '0',
  `questionAddress` varchar(255) NOT NULL DEFAULT '',
  `questionShopID` int(11) NOT NULL DEFAULT '0',
  `questionStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `question_postManID` (`questionPostManID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=60 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_setting`
--

CREATE TABLE IF NOT EXISTS `tb_setting` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `settingUserUseURL` varchar(255) NOT NULL DEFAULT '',
  `settingUserPiURL` varchar(255) NOT NULL DEFAULT '',
  `settingPointRule` varchar(255) NOT NULL DEFAULT '[]',
  `settingAppNoticeURL` varchar(255) NOT NULL DEFAULT '',
  `settingCastBannerRatio` double(16,11) NOT NULL DEFAULT '2.00000000000',
  `settingMainBannerRatio` double(16,11) NOT NULL DEFAULT '2.00000000000',
  `settingEventBannerRatio` double(16,11) NOT NULL DEFAULT '2.00000000000',
  `settingPushBannerRatio` double(16,11) NOT NULL DEFAULT '2.00000000000',
  `settingLogoutBannerRatio` double(16,11) NOT NULL DEFAULT '2.00000000000',
  `settingStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_shop`
--

CREATE TABLE IF NOT EXISTS `tb_shop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shopID` varchar(255) NOT NULL DEFAULT '',
  `shopPW` varchar(255) NOT NULL DEFAULT '',
  `shopName` varchar(255) NOT NULL DEFAULT '',
  `shopLongtitude` double(15,10) NOT NULL DEFAULT '0.0000000000',
  `shopLatitude` double(15,10) NOT NULL DEFAULT '0.0000000000',
  `shopAddress` varchar(255) NOT NULL DEFAULT '',
  `shopImgID` int(11) DEFAULT NULL,
  `shopCategoryID` int(11) NOT NULL DEFAULT '2',
  `shopOwnerID` int(11) NOT NULL DEFAULT '0',
  `shopPhonenumber` varchar(24) NOT NULL DEFAULT '',
  `shopParkable` varchar(1) NOT NULL DEFAULT 'N',
  `shopPostManName` varchar(255) NOT NULL DEFAULT '',
  `shopStuffPhoneNumber` varchar(255) NOT NULL DEFAULT '',
  `shopImgIDArrayString` varchar(1000) NOT NULL DEFAULT '[]',
  `shopQuestionable` varchar(1) NOT NULL DEFAULT 'N',
  `shopLevel` int(8) NOT NULL DEFAULT '0',
  `shopDiscountInfo` varchar(255) NOT NULL DEFAULT '',
  `shopEventable` varchar(1) NOT NULL DEFAULT 'Y',
  `shopBannerDays` int(11) NOT NULL DEFAULT '0',
  `shopCallCnt` int(11) NOT NULL DEFAULT '0',
  `shopClickCnt` int(11) NOT NULL DEFAULT '0',
  `shopManagerIdentyImgID` int(11) DEFAULT NULL,
  `shopOpenTimeString` varchar(1000) NOT NULL DEFAULT '',
  `shopRestTimeString` varchar(1000) NOT NULL DEFAULT '',
  `shopDescriptionString` varchar(10000) NOT NULL DEFAULT '',
  `shopAutoLogin` tinyint(1) NOT NULL DEFAULT '0',
  `shopRoad` varchar(1000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `shopPriceImgIDArrayString` varchar(1000) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '[]',
  `shopRequestTime` datetime NOT NULL DEFAULT '2015-08-15 00:00:00',
  `shopConsequence` int(11) NOT NULL DEFAULT '99999999',
  `shopLiveDate` int(11) NOT NULL DEFAULT '365',
  `shopStatus` tinyint(1) NOT NULL DEFAULT '3',
  `shopVisibility` int(1) NOT NULL DEFAULT '5',
  PRIMARY KEY (`id`),
  KEY `shop_imgID` (`shopImgID`),
  KEY `shop_categoryID` (`shopCategoryID`),
  KEY `shop_ownerID` (`shopOwnerID`),
  KEY `shop_managerIdentyImgID` (`shopManagerIdentyImgID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=104692 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_shopanswer`
--

CREATE TABLE IF NOT EXISTS `tb_shopanswer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shopanswerPostManID` int(11) NOT NULL,
  `shopanswerShopCommentID` int(11) NOT NULL,
  `shopanswerContent` varchar(1000) NOT NULL DEFAULT '',
  `shopanswerPostTime` datetime NOT NULL,
  `shopanswerStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `shopanswer_PostManID` (`shopanswerPostManID`),
  KEY `shopanswer_ShopCommentID` (`shopanswerShopCommentID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_shopcomment`
--

CREATE TABLE IF NOT EXISTS `tb_shopcomment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `shopcommentPostManID` int(11) NOT NULL,
  `shopcommentShopID` int(11) NOT NULL,
  `shopcommentContent` varchar(1000) NOT NULL DEFAULT '',
  `shopcommentPostTime` datetime NOT NULL,
  `shopcommentShopLevel` int(16) NOT NULL DEFAULT '0',
  `shopcommentType` varchar(2) NOT NULL DEFAULT 'UR',
  `shopcommentShopStatus` int(2) NOT NULL DEFAULT '-1',
  `shopcommentStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `shopcomment_PostManID` (`shopcommentPostManID`),
  KEY `shopcomment_ShopID` (`shopcommentShopID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=96 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_tube`
--

CREATE TABLE IF NOT EXISTS `tb_tube` (
  `id` int(8) unsigned NOT NULL AUTO_INCREMENT,
  `tubeName` varchar(255) NOT NULL DEFAULT '',
  `tubeSubName` varchar(255) NOT NULL DEFAULT '',
  `tubeLatitude` double(15,10) NOT NULL,
  `tubeLongtitude` double(15,10) NOT NULL,
  `tubeStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=864 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_user`
--

CREATE TABLE IF NOT EXISTS `tb_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userID` varchar(255) NOT NULL DEFAULT '',
  `userNickname` varchar(255) NOT NULL DEFAULT '',
  `userSex` varchar(1) NOT NULL DEFAULT 'M',
  `userAge` int(3) NOT NULL DEFAULT '10',
  `userLocationName` varchar(255) NOT NULL DEFAULT '',
  `userRecommenderID` varchar(255) NOT NULL DEFAULT '',
  `userPassword` varchar(255) NOT NULL DEFAULT '',
  `userOldPassword` varchar(255) NOT NULL DEFAULT '',
  `userKey` varchar(255) NOT NULL,
  `userEmail` varchar(255) NOT NULL DEFAULT '',
  `userSessionStatus` varchar(8) NOT NULL DEFAULT 'SU0000',
  `userNewEvent` varchar(1) NOT NULL DEFAULT 'N',
  `userNewInform` varchar(1) NOT NULL DEFAULT 'N',
  `userNewMessage` varchar(1) NOT NULL DEFAULT 'N',
  `userDeviceID` varchar(64) NOT NULL,
  `userDeviceModel` varchar(64) NOT NULL,
  `userOsType` varchar(16) NOT NULL,
  `userOsVersion` varchar(8) NOT NULL,
  `userAppVersion` varchar(8) NOT NULL DEFAULT '',
  `userImgID` int(11) DEFAULT NULL,
  `userPoint` int(11) NOT NULL DEFAULT '0',
  `userLevel` tinyint(1) NOT NULL DEFAULT '0',
  `userMarketID` int(11) NOT NULL,
  `userGCMRegID` varchar(255) NOT NULL DEFAULT '',
  `userRegisterTime` datetime NOT NULL,
  `userInviteFriendFBCnt` int(11) NOT NULL DEFAULT '0',
  `userInviteFriendKakaoCnt` int(11) NOT NULL DEFAULT '0',
  `userStatus` tinyint(1) NOT NULL DEFAULT '0',
  `userAccessed` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_imgID` (`userImgID`),
  KEY `user_marketid` (`userMarketID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=142 ;

-- --------------------------------------------------------

--
-- Table structure for table `tb_usershoprelation`
--

CREATE TABLE IF NOT EXISTS `tb_usershoprelation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usershoprelationUserID` int(11) NOT NULL,
  `usershoprelationShopID` int(11) NOT NULL,
  `usershoprelationJim` tinyint(1) NOT NULL DEFAULT '0',
  `usershoprelationStatus` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `usershoprelation_UserID` (`usershoprelationUserID`),
  KEY `usershoprelation_ShopID` (`usershoprelationShopID`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `tb_ads`
--
ALTER TABLE `tb_ads`
  ADD CONSTRAINT `ads_ImageID` FOREIGN KEY (`adsImgID`) REFERENCES `tb_image` (`id`);

--
-- Constraints for table `tb_app`
--
ALTER TABLE `tb_app`
  ADD CONSTRAINT `app_marketid` FOREIGN KEY (`appMarketID`) REFERENCES `tb_market` (`id`);

--
-- Constraints for table `tb_banner`
--
ALTER TABLE `tb_banner`
  ADD CONSTRAINT `banner_AdsID` FOREIGN KEY (`bannerAdsID`) REFERENCES `tb_ads` (`id`);

--
-- Constraints for table `tb_bannerclick`
--
ALTER TABLE `tb_bannerclick`
  ADD CONSTRAINT `bannerclick_BannerID` FOREIGN KEY (`bannerclickBannerID`) REFERENCES `tb_banner` (`id`);

--
-- Constraints for table `tb_bannercomment`
--
ALTER TABLE `tb_bannercomment`
  ADD CONSTRAINT `tb_bannercomment_ibfk_1` FOREIGN KEY (`bannercommentBannerID`) REFERENCES `tb_banner` (`id`),
  ADD CONSTRAINT `tb_bannercomment_ibfk_2` FOREIGN KEY (`bannercommentPostManID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_cast`
--
ALTER TABLE `tb_cast`
  ADD CONSTRAINT `cast_CategoryID` FOREIGN KEY (`castCategoryID`) REFERENCES `tb_category` (`id`),
  ADD CONSTRAINT `cast_CoveredImgID` FOREIGN KEY (`castCoveredImgID`) REFERENCES `tb_image` (`id`),
  ADD CONSTRAINT `cast_PostManID` FOREIGN KEY (`castPostManID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_castcomment`
--
ALTER TABLE `tb_castcomment`
  ADD CONSTRAINT `castcomment_CastID` FOREIGN KEY (`castcommentCastID`) REFERENCES `tb_cast` (`id`),
  ADD CONSTRAINT `castcomment_PostManID` FOREIGN KEY (`castcommentPostManID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_castcommentrelation`
--
ALTER TABLE `tb_castcommentrelation`
  ADD CONSTRAINT `castcommentrelation_CastCommentID` FOREIGN KEY (`castcommentrelationCastCommentID`) REFERENCES `tb_castcomment` (`id`),
  ADD CONSTRAINT `castcommentrelation_PostManID` FOREIGN KEY (`castcommentrelationPostManID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_castdetail`
--
ALTER TABLE `tb_castdetail`
  ADD CONSTRAINT `castdetail_CastID` FOREIGN KEY (`castdetailCastID`) REFERENCES `tb_cast` (`id`),
  ADD CONSTRAINT `castdetail_ImgID` FOREIGN KEY (`castdetailImgID`) REFERENCES `tb_image` (`id`);

--
-- Constraints for table `tb_castrelation`
--
ALTER TABLE `tb_castrelation`
  ADD CONSTRAINT `castrelation_CastID` FOREIGN KEY (`castrelationCastID`) REFERENCES `tb_cast` (`id`),
  ADD CONSTRAINT `castrelation_PostManID` FOREIGN KEY (`castrelationPostManID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_event`
--
ALTER TABLE `tb_event`
  ADD CONSTRAINT `event_imgID` FOREIGN KEY (`eventImgID`) REFERENCES `tb_image` (`id`),
  ADD CONSTRAINT `event_shopID` FOREIGN KEY (`eventShopID`) REFERENCES `tb_shop` (`id`);

--
-- Constraints for table `tb_freetalk`
--
ALTER TABLE `tb_freetalk`
  ADD CONSTRAINT `freetalk_categoryID` FOREIGN KEY (`freetalkCategoryID`) REFERENCES `tb_category` (`id`),
  ADD CONSTRAINT `freetalk_userID` FOREIGN KEY (`freetalkPostManID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_freetalkcomment`
--
ALTER TABLE `tb_freetalkcomment`
  ADD CONSTRAINT `freetalkcomment_FreetalkID` FOREIGN KEY (`freetalkcommentFreetalkID`) REFERENCES `tb_freetalk` (`id`),
  ADD CONSTRAINT `freetalkcomment_PostManID` FOREIGN KEY (`freetalkcommentPostManID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_freetalkcommentrelation`
--
ALTER TABLE `tb_freetalkcommentrelation`
  ADD CONSTRAINT `freetalkcommentrelation_FreetalkCommentID` FOREIGN KEY (`freetalkcommentrelationFreetalkCommentID`) REFERENCES `tb_freetalkcomment` (`id`),
  ADD CONSTRAINT `freetalkcommentrelation_PostManID` FOREIGN KEY (`freetalkcommentrelationPostManID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_freetalkcommenttag`
--
ALTER TABLE `tb_freetalkcommenttag`
  ADD CONSTRAINT `freetalkcommenttag_FreetalkCommentID` FOREIGN KEY (`freetalkcommenttagFreetalkCommentID`) REFERENCES `tb_freetalkcomment` (`id`),
  ADD CONSTRAINT `freetalkcommenttag_TAGUserID` FOREIGN KEY (`freetalkcommenttagTAGUserID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_freetalkrelation`
--
ALTER TABLE `tb_freetalkrelation`
  ADD CONSTRAINT `freetalkrelation_FreetalkID` FOREIGN KEY (`freetalkrelationFreetalkID`) REFERENCES `tb_freetalk` (`id`),
  ADD CONSTRAINT `freetalkrelation_PostManID` FOREIGN KEY (`freetalkrelationPostManID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_gcmlog`
--
ALTER TABLE `tb_gcmlog`
  ADD CONSTRAINT `gcmlog_userID` FOREIGN KEY (`gcmlogUserID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_image`
--
ALTER TABLE `tb_image`
  ADD CONSTRAINT `image_UploadUserID` FOREIGN KEY (`imageUploadUserID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_life`
--
ALTER TABLE `tb_life`
  ADD CONSTRAINT `life_adsID` FOREIGN KEY (`lifeAdsID`) REFERENCES `tb_ads` (`id`),
  ADD CONSTRAINT `life_categoryID` FOREIGN KEY (`lifeCategoryID`) REFERENCES `tb_category` (`id`);

--
-- Constraints for table `tb_lifeclick`
--
ALTER TABLE `tb_lifeclick`
  ADD CONSTRAINT `lifeclick_LifeID` FOREIGN KEY (`lifeclickLifeID`) REFERENCES `tb_life` (`id`);

--
-- Constraints for table `tb_product`
--
ALTER TABLE `tb_product`
  ADD CONSTRAINT `product_shopID` FOREIGN KEY (`productShopID`) REFERENCES `tb_shop` (`id`);

--
-- Constraints for table `tb_question`
--
ALTER TABLE `tb_question`
  ADD CONSTRAINT `question_postManID` FOREIGN KEY (`questionPostManID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_shop`
--
ALTER TABLE `tb_shop`
  ADD CONSTRAINT `shop_categoryID` FOREIGN KEY (`shopCategoryID`) REFERENCES `tb_category` (`id`),
  ADD CONSTRAINT `shop_imgID` FOREIGN KEY (`shopImgID`) REFERENCES `tb_image` (`id`),
  ADD CONSTRAINT `shop_managerIdentyImgID` FOREIGN KEY (`shopManagerIdentyImgID`) REFERENCES `tb_image` (`id`),
  ADD CONSTRAINT `shop_ownerID` FOREIGN KEY (`shopOwnerID`) REFERENCES `tb_user` (`id`);

--
-- Constraints for table `tb_shopanswer`
--
ALTER TABLE `tb_shopanswer`
  ADD CONSTRAINT `shopanswer_PostManID` FOREIGN KEY (`shopanswerPostManID`) REFERENCES `tb_user` (`id`),
  ADD CONSTRAINT `shopanswer_ShopCommentID` FOREIGN KEY (`shopanswerShopCommentID`) REFERENCES `tb_shopcomment` (`id`);

--
-- Constraints for table `tb_shopcomment`
--
ALTER TABLE `tb_shopcomment`
  ADD CONSTRAINT `shopcomment_PostManID` FOREIGN KEY (`shopcommentPostManID`) REFERENCES `tb_user` (`id`),
  ADD CONSTRAINT `shopcomment_ShopID` FOREIGN KEY (`shopcommentShopID`) REFERENCES `tb_shop` (`id`);

--
-- Constraints for table `tb_user`
--
ALTER TABLE `tb_user`
  ADD CONSTRAINT `user_imgID` FOREIGN KEY (`userImgID`) REFERENCES `tb_image` (`id`),
  ADD CONSTRAINT `user_marketid` FOREIGN KEY (`userMarketID`) REFERENCES `tb_market` (`id`);

--
-- Constraints for table `tb_usershoprelation`
--
ALTER TABLE `tb_usershoprelation`
  ADD CONSTRAINT `usershoprelation_ShopID` FOREIGN KEY (`usershoprelationShopID`) REFERENCES `tb_shop` (`id`),
  ADD CONSTRAINT `usershoprelation_UserID` FOREIGN KEY (`usershoprelationUserID`) REFERENCES `tb_user` (`id`);
