# Araba Galerisi Yönetim Sistemi (Car Gallery API)

Bu proje, Orta Düzey Programlama dersi final projesi kapsamında geliştirilmiş bir Araba Galerisi Yönetim Sistemi API'sidir. Proje, araçların veritabanına eklenmesi, güncellenmesi, silinmesi ve listelenmesi (CRUD) işlemlerini gerçekleştirmektedir.

## Kullanılan Teknolojiler
* **Java 17 & Spring Boot:** Temel uygulama iskeleti.
* **Spring Data JPA & Hibernate:** Veritabanı işlemleri ve Entity yönetimi.
* **H2 Database:** Geliştirme ortamı için bellek içi (in-memory) veritabanı.
* **Spring Security:** API uç noktalarının güvenliği ve Rol Tabanlı Erişim Kontrolü (RBAC).
* **Spring Boot Validation:** DTO nesneleri üzerinden gelen verilerin doğrulanması.

## Kurulum ve Çalıştırma
1. Projeyi bilgisayarınıza indirin (ZIP veya Git Clone).
2. Projeyi IntelliJ IDEA veya tercih ettiğiniz bir IDE ile açın.
3. Maven bağımlılıklarının yüklenmesini bekleyin.
4. `CargalleryApplication.java` sınıfını bularak projeyi başlatın (`Run`).
5. Proje varsayılan olarak `http://localhost:8080` portunda çalışacaktır.

## API Kullanımı ve Güvenlik
Projede Spring Security kullanılmıştır. Metotlara erişim için **Basic Auth** gereklidir:
* **Kullanıcı Rolü (Sadece Okuma - GET):** Username: `user` | Password: `1234`
* **Admin Rolü (Tam Yetki - CRUD):** Username: `admin` | Password: `admin123`