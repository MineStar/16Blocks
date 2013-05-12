CREATE TABLE IF NOT EXISTS `zones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `areaOwner` text NOT NULL,
  `x` int(11) NOT NULL,
  `z` int(11) NOT NULL,
  `creationDate` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
