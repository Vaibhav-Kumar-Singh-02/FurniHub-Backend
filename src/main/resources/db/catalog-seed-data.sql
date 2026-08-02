-- =====================================================================
-- FurniHub catalog data (idempotent / re-runnable)
-- Target: MySQL 8.x, schema ecommerce (see application.yml datasource)
-- Run manually, e.g.:
--   mysql -u root -p --host=127.0.0.1 --port=3307 ecommerce < db/catalog-seed-data.sql
--
-- Safe to run more than once. Uses INSERT ... WHERE NOT EXISTS guards
-- keyed on unique category names / product sku / primary image, so no
-- duplicates are created and no existing data is touched or removed.
-- No schema changes.
-- =====================================================================

USE ecommerce;

-- ---------------------------------------------------------------------
-- CATEGORIES (insert missing only; never delete or rename existing)
-- ---------------------------------------------------------------------
INSERT INTO categories (name, description, image_url, active)
SELECT 'Beds','Comfortable and stylish beds for every bedroom.','https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=600&q=80',1
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name='Beds');

INSERT INTO categories (name, description, image_url, active)
SELECT 'Wardrobes','Spacious wardrobes to organise your clothes in style.','https://images.unsplash.com/photo-1556228453-efd6c1ff04f6?w=600&q=80',1
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name='Wardrobes');

INSERT INTO categories (name, description, image_url, active)
SELECT 'Bookshelves','Bookshelves to display books and prized decor.','https://images.unsplash.com/photo-1583847268964-b28dc8f51f92?w=600&q=80',1
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name='Bookshelves');

INSERT INTO categories (name, description, image_url, active)
SELECT 'Dining Set','Complete dining tables and chairs for your home.','https://images.unsplash.com/photo-1493663284031-b7e3aefcae8e?w=600&q=80',1
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name='Dining Set');

INSERT INTO categories (name, description, image_url, active)
SELECT 'Office','Ergonomic office desks and chairs that keep you productive.','https://images.unsplash.com/photo-1592078615290-033ee584e267?w=600&q=80',1
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name='Office');

INSERT INTO categories (name, description, image_url, active)
SELECT 'TV Unit','TV units and media consoles for your entertainment area.','https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=600&q=80',1
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name='TV Unit');

INSERT INTO categories (name, description, image_url, active)
SELECT 'Storage','Storage racks and cabinets to declutter your space.','https://images.unsplash.com/photo-1449247709967-d4461a6a6103?w=600&q=80',1
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name='Storage');

INSERT INTO categories (name, description, image_url, active)
SELECT 'Decor','Wall decor and accent pieces to finish your room.','https://images.unsplash.com/photo-1505691938895-1758d7feb511?w=600&q=80',1
WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name='Decor');

-- ---------------------------------------------------------------------
-- PRODUCTS (keyed on unique sku; never duplicate)
-- ---------------------------------------------------------------------
INSERT INTO products (name, description, price, discount_price, stock_quantity, sku, category_id, active)
SELECT '3 Door Sliding Wardrobe','Solid engineered wood 3-door sliding wardrobe with soft-close hinges, full-length mirror and generous hanging space.',37999.00,34999.00,12,'WRD-001',c.id,1
FROM categories c WHERE c.name='Wardrobes'
AND NOT EXISTS (SELECT 1 FROM products WHERE sku='WRD-001');

INSERT INTO products (name, description, price, discount_price, stock_quantity, sku, category_id, active)
SELECT '4 Door Wardrobe','Spacious 4-door wardrobe in walnut finish with three full shelves and anti-rust metal handles.',58999.00,NULL,7,'WRD-002',c.id,1
FROM categories c WHERE c.name='Wardrobes'
AND NOT EXISTS (SELECT 1 FROM products WHERE sku='WRD-002');

INSERT INTO products (name, description, price, discount_price, stock_quantity, sku, category_id, active)
SELECT 'King Size Bed with Storage','Solid sheesham king bed with hydraulic storage lifters and a cushioned headboard.',45999.00,42999.00,10,'BED-001',c.id,1
FROM categories c WHERE c.name='Beds'
AND NOT EXISTS (SELECT 1 FROM products WHERE sku='BED-001');

INSERT INTO products (name, description, price, discount_price, stock_quantity, sku, category_id, active)
SELECT 'Queen Size Fabric Bed','Upholstered queen bed in grey linen with two side drawers and matching platform base.',38999.00,NULL,15,'BED-002',c.id,1
FROM categories c WHERE c.name='Beds'
AND NOT EXISTS (SELECT 1 FROM products WHERE sku='BED-002');

INSERT INTO products (name, description, price, discount_price, stock_quantity, sku, category_id, active)
SELECT 'Engineered Book Shelf','Five-tier open bookshelf in matt teak finish with strong base for your library.',19999.00,18999.00,22,'BSH-001',c.id,1
FROM categories c WHERE c.name='Bookshelves'
AND NOT EXISTS (SELECT 1 FROM products WHERE sku='BSH-001');

INSERT INTO products (name, description, price, discount_price, stock_quantity, sku, category_id, active)
SELECT 'Corner Book Shelf','Compact four-tier corner shelf that makes the most of unused space.',12999.00,NULL,18,'BSH-002',c.id,1
FROM categories c WHERE c.name='Bookshelves'
AND NOT EXISTS (SELECT 1 FROM products WHERE sku='BSH-002');

INSERT INTO products (name, description, price, discount_price, stock_quantity, sku, category_id, active)
SELECT 'Sheesham Dining Set','6-seater sheesham dining table with four cushioned dining chairs in a warm honey finish.',67999.00,63999.00,5,'DIN-001',c.id,1
FROM categories c WHERE c.name='Dining Set'
AND NOT EXISTS (SELECT 1 FROM products WHERE sku='DIN-001');

INSERT INTO products (name, description, price, discount_price, stock_quantity, sku, category_id, active)
SELECT 'Ergonomic Office Desk','Spacious office desk with cable management and durable laminated top for work-from-home.',21999.00,NULL,20,'OFF-001',c.id,1
FROM categories c WHERE c.name='Office'
AND NOT EXISTS (SELECT 1 FROM products WHERE sku='OFF-001');

INSERT INTO products (name, description, price, discount_price, stock_quantity, sku, category_id, active)
SELECT 'Adjustable Office Chair','Mesh-back adjustable office chair with lumbar support and 360-degree swivel.',16999.00,15999.00,30,'OFF-002',c.id,1
FROM categories c WHERE c.name='Office'
AND NOT EXISTS (SELECT 1 FROM products WHERE sku='OFF-002');

INSERT INTO products (name, description, price, discount_price, stock_quantity, sku, category_id, active)
SELECT 'Walnut TV Unit','Walnut wood TV unit with open display shelves and two drawers for media storage.',22999.00,NULL,14,'TVU-001',c.id,1
FROM categories c WHERE c.name='TV Unit'
AND NOT EXISTS (SELECT 1 FROM products WHERE sku='TVU-001');

INSERT INTO products (name, description, price, discount_price, stock_quantity, sku, category_id, active)
SELECT 'Almirah Storage Cabinet','Two-door metal almirah with locking doors and three shelves of robust storage.',15999.00,NULL,26,'STR-001',c.id,1
FROM categories c WHERE c.name='Storage'
AND NOT EXISTS (SELECT 1 FROM products WHERE sku='STR-001');

INSERT INTO products (name, description, price, discount_price, stock_quantity, sku, category_id, active)
SELECT 'Wall Art Decor Set','Set of three abstract canvas wall art panels in muted tones to warm any wall.',4999.00,NULL,40,'DEC-001',c.id,1
FROM categories c WHERE c.name='Decor'
AND NOT EXISTS (SELECT 1 FROM products WHERE sku='DEC-001');

-- ---------------------------------------------------------------------
-- PRODUCT IMAGES (primary only, idempotent per product)
-- ---------------------------------------------------------------------
INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id,'https://images.unsplash.com/photo-1556228453-efd6c1ff04f6?w=600&q=80',1,0 FROM products p WHERE p.sku='WRD-001'
AND NOT EXISTS (SELECT 1 FROM product_images i WHERE i.product_id=p.id AND i.is_primary=1);

INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id,'https://images.unsplash.com/photo-1449247709967-d4461a6a6103?w=600&q=80',1,0 FROM products p WHERE p.sku='WRD-002'
AND NOT EXISTS (SELECT 1 FROM product_images i WHERE i.product_id=p.id AND i.is_primary=1);

INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id,'https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=600&q=80',1,0 FROM products p WHERE p.sku='BED-001'
AND NOT EXISTS (SELECT 1 FROM product_images i WHERE i.product_id=p.id AND i.is_primary=1);

INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id,'https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=600&q=80',1,0 FROM products p WHERE p.sku='BED-002'
AND NOT EXISTS (SELECT 1 FROM product_images i WHERE i.product_id=p.id AND i.is_primary=1);

INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id,'https://images.unsplash.com/photo-1583847268964-b28dc8f51f92?w=600&q=80',1,0 FROM products p WHERE p.sku='BSH-001'
AND NOT EXISTS (SELECT 1 FROM product_images i WHERE i.product_id=p.id AND i.is_primary=1);

INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id,'https://images.unsplash.com/photo-1519710164239-da123dc03ef4?w=600&q=80',1,0 FROM products p WHERE p.sku='BSH-002'
AND NOT EXISTS (SELECT 1 FROM product_images i WHERE i.product_id=p.id AND i.is_primary=1);

INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id,'https://images.unsplash.com/photo-1493663284031-b7e3aefcae8e?w=600&q=80',1,0 FROM products p WHERE p.sku='DIN-001'
AND NOT EXISTS (SELECT 1 FROM product_images i WHERE i.product_id=p.id AND i.is_primary=1);

INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id,'https://images.unsplash.com/photo-1592078615290-033ee584e267?w=600&q=80',1,0 FROM products p WHERE p.sku='OFF-001'
AND NOT EXISTS (SELECT 1 FROM product_images i WHERE i.product_id=p.id AND i.is_primary=1);

INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id,'https://images.unsplash.com/photo-1592078615290-033ee584e267?w=600&q=80',1,0 FROM products p WHERE p.sku='OFF-002'
AND NOT EXISTS (SELECT 1 FROM product_images i WHERE i.product_id=p.id AND i.is_primary=1);

INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id,'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?w=600&q=80',1,0 FROM products p WHERE p.sku='TVU-001'
AND NOT EXISTS (SELECT 1 FROM product_images i WHERE i.product_id=p.id AND i.is_primary=1);

INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id,'https://images.unsplash.com/photo-1449247709967-d4461a6a6103?w=600&q=80',1,0 FROM products p WHERE p.sku='STR-001'
AND NOT EXISTS (SELECT 1 FROM product_images i WHERE i.product_id=p.id AND i.is_primary=1);

INSERT INTO product_images (product_id, image_url, is_primary, display_order)
SELECT p.id,'https://images.unsplash.com/photo-1505691938895-1758d7feb511?w=600&q=80',1,0 FROM products p WHERE p.sku='DEC-001'
AND NOT EXISTS (SELECT 1 FROM product_images i WHERE i.product_id=p.id AND i.is_primary=1);