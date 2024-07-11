-- Создание таблицы product
CREATE TABLE IF NOT EXISTS public.product
(
    id                BIGSERIAL PRIMARY KEY,
    description       VARCHAR(50)    NOT NULL,
    price             DECIMAL(10, 2) NOT NULL,
    quantity_in_stock INTEGER        NOT NULL,
    wholesale_product BOOLEAN        NOT NULL
);

-- Создание таблицы discount_card
CREATE TABLE IF NOT EXISTS public.discount_card
(
    id     BIGSERIAL PRIMARY KEY,
    number INTEGER  NOT NULL UNIQUE,
    amount SMALLINT NOT NULL
);

-- Добавление ограничения на amount в таблице discount_card
ALTER TABLE public.discount_card
    ADD CONSTRAINT check_amount CHECK (amount >= 0 AND amount <= 100);

-- Вставка данных в таблицу product
INSERT INTO public.product (id, description, price, quantity_in_stock, wholesale_product)
VALUES (1, 'Milk', 1.07, 10, true),
       (2, 'Cream 400g', 2.71, 20, true),
       (3, 'Yogurt 400g', 2.10, 7, true),
       (4, 'Packed potatoes 1kg', 1.47, 30, false),
       (5, 'Packed cabbage 1kg', 1.19, 15, false),
       (6, 'Packed tomatoes 350g', 1.60, 50, false),
       (7, 'Packed apples 1kg', 2.78, 18, false),
       (8, 'Packed oranges 1kg', 3.20, 12, false),
       (9, 'Packed bananas 1kg', 1.10, 25, true),
       (10, 'Packed beef fillet 1kg', 12.80, 7, false),
       (11, 'Packed pork fillet 1kg', 8.52, 14, false),
       (12, 'Packed chicken breasts 1kgSour', 10.75, 18, false),
       (13, 'Baguette 360g', 1.30, 10, true),
       (14, 'Drinking water 1,5l', 0.80, 100, false),
       (15, 'Olive oil 500ml', 5.30, 16, false),
       (16, 'Sunflower oil 1l', 1.20, 12, false),
       (17, 'Chocolate Ritter sport 100g', 1.10, 50, true),
       (18, 'Paulaner 0,5l', 1.10, 100, false),
       (19, 'Whiskey Jim Beam 1l', 13.99, 30, false),
       (20, 'Whiskey Jack Daniels 1l', 17.19, 20, false);

-- Обновление последовательности id для таблицы product
SELECT setval('public.product_id_seq', (SELECT MAX(id) FROM public.product));

-- Вставка данных в таблицу discount_card
INSERT INTO public.discount_card (id, number, amount)
VALUES (1, 1111, 3),
       (2, 2222, 3),
       (3, 3333, 4),
       (4, 4444, 5);

-- Обновление последовательности id для таблицы discount_card
SELECT setval('public.discount_card_id_seq', (SELECT MAX(id) FROM public.discount_card));