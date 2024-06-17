insert into "lodge" (id, amenities, approval_type, location, maximal_guest_number, minimal_guest_number, name, owner_id)
values ('b86553e1-2552-41cb-9e40-7ef87c424850', 'wifi,bazen', 'AUTOMATIC', 'Lokacija1', '3', '1', 'Vikendica', 'e49fcab5-d45b-4556-9d91-14e58177fea6'),
        ('b86553e1-2552-41cb-9e40-7ef87c424852', 'wifi,bazen', 'AUTOMATIC', 'Lokacija2', '3', '1', 'Vikendica2', 'e49fcab5-d45b-4556-9d91-14e58177fea1');

insert into "lodge_availability_period" (id, date_from, date_to, price, price_type, lodge_id)
values ('fb809d54-332d-4811-8d93-d3ddf2f345a2', '2024-06-12 20:10:21.263221', '2024-06-14 20:10:21.263221', '40.1', 'PER_LODGE', 'b86553e1-2552-41cb-9e40-7ef87c424850'),
		('6d612ca2-feb7-4e19-9db3-b4ce0ef3d2f0', '2024-06-15 20:10:21.263221', '2024-06-18 20:10:21.263221', '40.1', 'PER_LODGE', 'b86553e1-2552-41cb-9e40-7ef87c424850'),
		('4d612ca2-feb7-4e19-9db3-b4ce0ef3d2f1', '2024-06-15 20:10:21.263221', '2024-06-18 20:10:21.263221', '40.1', 'PER_LODGE', 'b86553e1-2552-41cb-9e40-7ef87c424852');