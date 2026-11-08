CREATE DATABASE IF NOT EXISTS svg_temp_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE svg_temp_db;

DROP TABLE IF EXISTS conversion;
DROP TABLE IF EXISTS unit_translation;
DROP TABLE IF EXISTS unit;
DROP TABLE IF EXISTS temp_record;
DROP TABLE IF EXISTS temperature_unit;

CREATE TABLE unit (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE unit_translation (
    unit_id     INT NOT NULL,
    lang        VARCHAR(5) NOT NULL,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    PRIMARY KEY (unit_id, lang),
    CONSTRAINT fk_unit_translation_unit FOREIGN KEY (unit_id)
        REFERENCES unit(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE conversion (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    input_value  DOUBLE NOT NULL,
    from_unit_id INT NOT NULL,
    to_unit_id   INT NOT NULL,
    result_value DOUBLE NOT NULL,
    lang         VARCHAR(5) NOT NULL,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_conversion_from FOREIGN KEY (from_unit_id) REFERENCES unit(id),
    CONSTRAINT fk_conversion_to   FOREIGN KEY (to_unit_id)   REFERENCES unit(id)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

INSERT INTO unit (code) VALUES
    ('celsius'),
    ('fahrenheit'),
    ('kelvin');

INSERT INTO unit_translation (unit_id, lang, name, description) VALUES
    (1, 'en', 'Celsius',    'Degrees Celsius'),
    (1, 'fi', 'Celsius',    'Celsiusastetta'),
    (1, 'sv', 'Celsius',    'Grader Celsius'),
    (1, 'ja', '摂氏',        '摂氏温度'),
    (1, 'ar', 'مئوية',       'درجة مئوية'),

    (2, 'en', 'Fahrenheit', 'Degrees Fahrenheit'),
    (2, 'fi', 'Fahrenheit', 'Fahrenheit-astetta'),
    (2, 'sv', 'Fahrenheit', 'Grader Fahrenheit'),
    (2, 'ja', '華氏',        '華氏温度'),
    (2, 'ar', 'فهرنهايت',    'درجة فهرنهايت'),

    (3, 'en', 'Kelvin',     'Kelvin, the SI unit of temperature'),
    (3, 'fi', 'Kelvin',     'Kelvin, lämpötilan SI-yksikkö'),
    (3, 'sv', 'Kelvin',     'Kelvin, SI-enheten för temperatur'),
    (3, 'ja', 'ケルビン',     '温度のSI単位'),
    (3, 'ar', 'كلفن',        'وحدة قياس الحرارة في النظام الدولي');
