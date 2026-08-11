-- 1. Insert Categories
INSERT IGNORE INTO categories (categorie_id, category_name, description) VALUES
(1, 'Living Room', 'Comfortable sofas, armchairs, and coffee tables for your living space'),
(2, 'Bedroom', 'Cozy beds, nightstands, and dressers for a restful sleep'),
(3, 'Dining', 'Elegant dining tables, chairs, and sideboards for memorable meals'),
(4, 'Office', 'Ergonomic desks and office chairs for maximum productivity'),
(5, 'Decor', 'Stylish lamps, rugs, and decorative accessories');

-- 2. Insert Products
INSERT IGNORE INTO products (product_id, name, brand, categorie_id, subcategory, description, price, discount, stock, ratings, status) VALUES
(1, 'Modern Velvet Sofa', 'FurniHub Luxury', 1, 'Sofas', 'Plush 3-seater velvet sofa with solid hardwood frame and high-density foam cushions.', 899.99, 10, 15, 4.8, 'ACTIVE'),
(2, 'Minimalist Wooden Coffee Table', 'FurniHub Craft', 1, 'Tables', 'Handcrafted oak coffee table with sleek rounded edges and open storage shelf.', 249.99, 5, 25, 4.6, 'ACTIVE'),
(3, 'Ergonomic Lounge Armchair', 'FurniHub Comfort', 1, 'Chairs', 'Premium leather reclining lounge chair with lumbar support and ottoman.', 450.00, 15, 10, 4.9, 'ACTIVE'),
(4, 'King Size Wooden Bed Frame', 'FurniHub Sleep', 2, 'Beds', 'Solid teak wood bed frame with built-in headboard and sturdy wooden slats.', 1199.99, 12, 8, 4.7, 'ACTIVE'),
(5, '2-Drawer Nightstand', 'FurniHub Sleep', 2, 'Nightstands', 'Compact nightstand with soft-close drawers and brass handles.', 149.99, 0, 30, 4.5, 'ACTIVE'),
(6, '6-Person Solid Wood Dining Table', 'FurniHub Living', 3, 'Dining Tables', 'Spacious walnut dining table perfect for family gatherings.', 799.99, 10, 12, 4.9, 'ACTIVE'),
(7, 'Upholstered Dining Chair (Set of 2)', 'FurniHub Living', 3, 'Dining Chairs', 'Set of 2 cushioned dining chairs with mid-century tapered legs.', 199.99, 5, 20, 4.7, 'ACTIVE'),
(8, 'Adjustable Standing Desk', 'FurniHub Work', 4, 'Desks', 'Electric height-adjustable desk with dual motor and memory presets.', 499.99, 15, 18, 4.8, 'ACTIVE'),
(9, 'Ergonomic Mesh Office Chair', 'FurniHub Work', 4, 'Chairs', 'Breathable mesh chair with 3D armrests and lumbar support.', 299.99, 10, 22, 4.6, 'ACTIVE'),
(10, 'Nordic Ceramic Table Lamp', 'FurniHub Decor', 5, 'Lighting', 'Warm ambient ceramic table lamp with linen shade.', 79.99, 0, 40, 4.8, 'ACTIVE');

-- 3. Insert Product Images
INSERT IGNORE INTO productimages (image_id, product_id, image_url) VALUES
(1, 1, 'https://images.unsplash.com/photo-1555041469-a586c61ea9bc?auto=format&fit=crop&w=800&q=80'),
(2, 2, 'https://images.unsplash.com/photo-1533090161767-e6ffed986c88?auto=format&fit=crop&w=800&q=80'),
(3, 3, 'https://images.unsplash.com/photo-1567538096630-e0c55bd6374c?auto=format&fit=crop&w=800&q=80'),
(4, 4, 'https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?auto=format&fit=crop&w=800&q=80'),
(5, 5, 'https://images.unsplash.com/photo-1532372576444-dda954194ad0?auto=format&fit=crop&w=800&q=80'),
(6, 6, 'https://images.unsplash.com/photo-1615066390971-03e4e1c36ddf?auto=format&fit=crop&w=800&q=80'),
(7, 7, 'https://images.unsplash.com/photo-1503602642458-232111445657?auto=format&fit=crop&w=800&q=80'),
(8, 8, 'https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?auto=format&fit=crop&w=800&q=80'),
(9, 9, 'https://images.unsplash.com/photo-1580481072645-022f9a6d1265?auto=format&fit=crop&w=800&q=80'),
(10, 10, 'https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&w=800&q=80');

-- 4. Insert Default Admin User (Email: admin@furnihub.com | Password: admin123)
INSERT IGNORE INTO admin_users (admin_id, username, full_name, email, mobile, password, role) 
VALUES (1, 'admin', 'Admin User', 'admin@furnihub.com', '9999999999', '$2a$10$1qkdcsErdmX/4KVui1Fgv.jNGbkNFOOFJWlgiBz3JqzpVlLP6uNSq', 'ADMIN');

INSERT IGNORE INTO users (user_id, username, full_name, email, mobile, password, role) 
VALUES (1, 'admin', 'Admin User', 'admin@furnihub.com', '9999999999', '$2a$10$rH8qZ7xY5wE3vR2tY7uI.eKjH8gF4dS2aB3cD4eF5gH6iJ7kL8mN9', 'ADMIN');
