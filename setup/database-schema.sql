CREATE TABLE `mcrpg_user`
(
    `uuid`                     binary(16) NOT NULL,
    `ability_points`           mediumint  UNSIGNED DEFAULT 0,
    `redeemable_exp`           int        UNSIGNED DEFAULT 0,
    `redeemable_levels`        mediumint  UNSIGNED DEFAULT 0,
    `power_level`              mediumint  UNSIGNED DEFAULT 0,
    `replace_ability_cooldown` timestamp,

    PRIMARY KEY (`uuid`)
);

CREATE TABLE `mcrpg_user_ability_data`
(
    `uuid`        binary(16) NOT NULL,
    `ability_id`  tinytext   NOT NULL,
    `tier`        integer    NOT NULL DEFAULT 0,
    `cooldown`    timestamp  NOT NULL,
    `toggled`     boolean    NOT NULL DEFAULT 1,

    PRIMARY KEY (`uuid`, `ability_id`),
    FOREIGN KEY (`uuid`) REFERENCES mcrpg_user(`uuid`)
);