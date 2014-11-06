INSERT INTO `STATUS` VALUES (1,'ACTIVE',NULL),(2,'INACTIVE',NULL);
INSERT INTO `USERS` VALUES (1,1,'e1','2f448266fef37f1fd1f3dbabf258c81b1bb19850f759818560d19667b51bf09911f8deccf07232f0d057b3102d25c655e851369e54ae9440c0885a5d3452051c'),(2,1,'e2','6602e08c09c6865e743271e8ce9006021afc91d0c7b89b5ecde757b5517e6e004a86024e4b78f8a184beaa8dca513a9d2d5cae93634d29a34c5d75ef7c74e210'),(3,1,'c1','bcfe21e6280bf11b8fdbd045c8b3e527748d1ecf18d1c44a9be6cd054107c447988d086514f6d59be950c5fc729e88f66d58c0e57440493b08b09b32f21f073b'),(4,1,'c2','a3bd3516b9cfa9a9a081db2d6a9aca82ff3bef0e8e10b104c13c7acf1ff1d39e8d09b63b6804adf09a7dd42efcb924b09e88a3a2d1c7c2bf6954bddbe251f0a9'),(5,1,'est0','f5605af98942136fcdb59b331b1a9c4de836787036c4b3b8c0fe0246bee1eb7e813785e453b4bac3d38f9c49cb19d694bc9983d81b890e74931c0e14a82c3464'),(6,1,'est1','4d2df64725cdaf37894a9519f78605a34ff4db39e47f5810bd385335360125d15f2d0b9edff922e857e850ce5f2a7d685ca292f34c5dc3e8725023732fd0485a'),(7,1,'est2','58051020e2e5f8b280b0b2a095fc129f95b3373da981e7dddb2944495c907fe61adbc28728e153f6dae77c5ed6b61c4610587386c06d07d934e31b5367bc9b03'),(8,1,'est3','3b3c712416cd4b3b69e670e2a15c4f220d740f12b1f8ead2195c8fa9d928af3788b664620f0e5f7f4298740b706e5f642442f2f1cec52aedafb00255657dc9da'),(9,1,'est4','9c5a355be1595911ba3e1b2db693e82dd04360203a433a99842b2b6008bdbc5cd4421423f1509ff20218dec85a9ee9f6868d6db43f93ec6e1b377da22cfb09ed'),(10,1,'est5','e056fd8734a6ddf563f20d2b009fbee411dad6646915812b98729fc91f7c096a5795c7fd402499fb595dd4b6d87021df4e88d70f78e13eb67f39e4dce59b9f61'),(11,1,'est6','c88c1cd41b607b55942ec389d8819e603b7ee00b6d03ec6b6b2842115376f00d66c2ccd40736f04f62d19b1e3ed89f534e7f72696365e20ea475eeaf155a3780'),(12,1,'est7','567afadeb898b69c7944abe0ee3faaa2c4d26ba60c3d40547d3f11e37c5a41fcc2b29c9e9225cb47de2ea4da0775b8f38ea85d035066abcea020b412a05caa05'),(13,1,'est8','9371d9301b99915d45877d46ff940b6c68595c4138ad72677bd0f6e42a65999ce34538113a28506af15690de2910d27adf46f0ffe596bfb224d30bbe0dc34440'),(14,1,'est9','5cb8d867bcd830695fd8fe3c556852d5458a817f0d83d1bc5fe49e27610775a3063d66524703e6ac589556828d7329d0ef90f253725cf55cb7cfd065545e1e1c'),(15,1,'est10','24e99233ab74716140ab3deb04f29093b93290201f295bf3ccc459ffc534bc920796d763fa96d068d8ed34679bece9b569044e0c67ab6b635814a98bea364f90'),(16,1,'est11','d4f5eda3d8692ea0589bc4c0fd2442c3fd7e8ddf0fce469dfdad7089d7fbb60eb0ae5e97f5daafc14122828e1bf11171cb68220c20774b2281cb98b167be3217');
INSERT INTO `ROLES` VALUES (1,'ESTABLISHMENT',NULL),(2,'CLIENT',NULL);
INSERT INTO `USERS_ROLES` VALUES (1,1),(2,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(15,1),(16,1),(3,2),(4,2);
INSERT INTO `SPECIALITIES` VALUES (1,'BRAZILIAN'),(2,'JAPANESE'),(3,'MEXICAN'),(4,'BAKERY'),(5,'SNACKS');
INSERT INTO `ESTABLISHMENTS` VALUES (1,1,1,'Padaria do Ze','Avenida Lins de Vasconcelos, São Paulo - SP, República Federativa do Brasil','Padaria grande com muitas variedades!',-23.57294010,-46.62280900,'5511111111111',NULL),(2,2,2,'Mercado da Ana','Rua Tito Oliani 1000','Mercado pequeno',-23.63005300,-46.58865200,'5511111111112',NULL),(3,5,3,'Consulado Mineiro I','Praça Benedito Calixto, 74 - Pinheiros, São Paulo - SP, 05406-040, República Federativa do Brasil','Restaurante',-23.55853000,-46.68030260,'5511111111113',NULL),(4,6,4,'Tuju','Rua Fradique Coutinho, 1248 - Pinheiros, São Paulo - SP, 05416-001, República Federativa do Brasil','Restaurante',-23.55674730,-46.69095460,'5511111111114',NULL),(5,7,5,'Vituccio Pizzeria','Rua Tonelero, 609 - Vila Ipojuca, São Paulo - SP, República Federativa do Brasil','Pizzaria',-23.53338570,-46.70430150,'5511111111115',NULL),(6,8,1,'Oui','Rua Vupabussu, 71 - Pinheiros, São Paulo - SP, 05429-040, República Federativa do Brasil','Restaurante frances',-23.55989880,-46.69729530,'5511111111116',NULL),(7,9,2,'Beato','Rua dos Pinheiros, 174 - Pinheiros, São Paulo - SP, República Federativa do Brasil','Restaurante variado',-23.56447570,-46.68007950,'5511111111117',NULL),(8,10,3,'America Jardins','Avenida Nove de Julho, 5363 - Jardim Paulista, São Paulo - SP, 01406-200, República Federativa do Brasil','Restaurante',-23.57937960,-46.67661400,'5511111111118',NULL),(9,11,4,'Cantina D\'Angelo','Rua Humaitá, 258 - Bela Vista, São Paulo - SP, 01321-010, República Federativa do Brasil','Cantina Italiana',-23.55862870,-46.64100040,'5511111111119',NULL),(10,12,5,'Ruaa','Rua Mourato Coelho, 1168 - Pinheiros, São Paulo - SP, 05417-002, República Federativa do Brasil','Restaurante',-23.55783300,-46.69155420,'5511111111121',NULL),(11,13,1,'Chez Mis','Avenida Europa, 158 - Jardim Europa, São Paulo - SP, República Federativa do Brasil','Restaurante',-23.57315680,-46.67601760,'5511111111122',NULL),(12,14,2,'Chacara Santa Cecilia','Rua Ferreira de Araújo, 601 - Pinheiros, São Paulo - SP, 05428-001, República Federativa do Brasil','Restaurante',-23.56300970,-46.69803420,'5511111111123',NULL),(13,15,3,'Cafe Journal','Alameda dos Anapurus, 1121 - Indianópolis, São Paulo - SP, 04087-003, República Federativa do Brasil','Restaurante',-23.60894430,-46.65951930,'5511111111124',NULL),(14,16,4,'Arturito','Rua Artur de Azevedo, 542 - Cerqueira César, São Paulo - SP, 05404-001, República Federativa do Brasil','Restaurante',-23.56010060,-46.67746800,'5511111111125',NULL);
INSERT INTO `CLIENTS` VALUES (1,3,'Fulano','1990-10-05','fulano@gmail.com','1'),(2,4,'Ciclano','1980-11-01','ciclano@gmail.com','1');
INSERT INTO `PROMOTIONS` VALUES (1,1,'X-Salada',3,'Na compra do terceiro X-Salada, ganhe outro de graça','2014-06-25 01:25:23','',NULL),(2,1,'Casquinha',4,'Junte 4 selos e ganhe uma casquinha','2014-06-25 01:25:23','\0',NULL),(3,2,'Parmegiana',8,'Na oitava compra da parmegiana, ganhe 10% de desconto em qualquer produto','2014-10-02 23:08:00','',NULL),(4,3,'Feijoada - 20%',1,'Ganhe 20% de desconto na feijoada!','2014-10-14 17:55:18','',NULL),(5,3,'Refri Grátis',5,'Compre 5 refrigerantes, e ganhe outro grátis','2014-10-14 17:56:13','',NULL);
INSERT INTO `CLIENTS_PROMOTIONS` VALUES (1,1,1,3,'','2014-10-14 23:17:10'),(2,1,2,2,'\0','2014-06-25 01:25:23'),(3,2,1,1,'\0','2014-06-25 01:25:23'),(4,1,5,5,'','2014-10-14 17:56:32'),(5,1,5,4,'\0','2014-10-14 17:56:32'),(6,1,1,3,'','2014-10-14 23:17:10'),(7,1,1,2,'\0','2014-10-14 23:17:11');
INSERT INTO `CLIENTS_PROMOTIONS_HISTORY` VALUES (1,1,1,'2014-06-01 20:05:00',1),(1,1,1,'2014-06-02 10:55:00',1),(1,1,1,'2014-06-02 18:25:00',1),(2,1,2,'2014-06-01 00:01:00',1),(2,1,2,'2014-06-01 07:07:00',1),(3,2,1,'2014-06-02 11:06:00',1);
