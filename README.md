#### Proje Amacı

Restoran projesinin ana amacı, restoran yönetimini kolaylaştırmak ve verimliliği artırmaktır. Bu proje, siparişlerin takibi, masa yönetimi ve raporlamayı entegre bir şekilde sağlayahilir.

#### Projenin Kapsamı

Bu proje, restoran yönetiminde ihtiyaç duyulan temel işlevleri sunan bir masaüstü uygulamasıdır. Proje kapsamında sipariş yönetimi, masa yönetimi ve raporlama gibi modüller bulunmaktadır. Her modül, restoran operasyonlarını daha düzenli ve kontrol edilebilir hale getirmek için tasarlanmıştır.

#### Proje Tasarımı

**1. Kullanıcı Arayüzü Tasarımı**

Kullanıcılar, sipariş ekleme, sipariş silme, masa durumu güncelleme ve rapor oluşturma işlemlerini kolayca gerçekleştirebilirler. Ana bileşenler şunlardır:
- **Sipariş Yönetim Paneli:** Kullanıcıların yeni siparişler ekleyebileceği, mevcut siparişleri görüntüleyip silebileceği bir panel.
- **Masa Yönetim Paneli:** Restorandaki masaların durumlarını (boş, dolu) güncelleyebilecekleri bir arayüz.
- **Menü Yönetim Paneli:** Restorandaki menü ürünlerinin oluşturulabileceği bir alan.
- **Envanter Yönetim Paneli:** Restorandaki envanter (depo) ürünlerinin saklanabileceği bir arayüz.
- **Raporlama Paneli:** Belirli tarih aralıklarına göre sipariş raporları oluşturabilecekleri bir alan.

**2. Veritabanı Tasarımı**

Projenin veritabanı, restoranın operasyonlarını desteklemek üzere optimize edilmiştir. Temel veritabanı tabloları şunlardır:
- **masa:** Masaların bilgilerini (id, masa_durumu) tutar.
- **siparisler:** Sipariş bilgilerini (id, masa_id, menu_urun_id, miktar, durum, created_at) tutar.

Veritabanı ilişkileri, siparişlerin ilgili masalarla ilişkilendirilmesini sağlar. Bu ilişkiler, verilerin bütünlüğünü ve tutarlılığını korur.

**3. İşlevsellik**

Projenin işlevselliği, kullanıcılara restoran operasyonlarını kolayca yönetme imkanı sağlar:
- **Sipariş Ekleme ve Silme:** Kullanıcılar, belirli bir masaya sipariş ekleyebilir veya mevcut bir siparişi silebilirler. Bu işlemler, veritabanı ile senkronize edilerek anlık güncellemeler sağlar.
- **Masa Durumu Güncelleme:** Kullanıcılar, masaların durumunu güncelleyerek restoranın anlık doluluk durumunu izleyebilirler.
- **Rapor Oluşturma:** Kullanıcılar, belirli tarih aralıklarına göre sipariş raporları oluşturup değerlendirilebilirler.

**4. Teknik Detaylar**

Proje, Java programlama dili ile geliştirilmiştir ve MySQL veritabanı kullanmaktadır. JDBC (Java Database Connectivity) kullanılarak veritabanı bağlantısı sağlanmıştır. Swing kütüphanesi, kullanıcı arayüzü bileşenlerinin oluşturulmasında kullanılmıştır.

#### Sonuç

Restoran projesi, restoran yönetimini daha etkin ve verimli hale getirmek için tasarlanmış kapsamlı bir çözümdür. Kullanıcı dostu arayüzü ve güçlü veritabanı yapısı ile restoran çalışanlarının iş süreçlerini kolaylaştırır. Bu proje, restoranların günlük operasyonlarını daha düzenli ve kontrol edilebilir hale getirerek, iş verimliliğini ve karlılığı artırmayı amaçlamaktadır.
