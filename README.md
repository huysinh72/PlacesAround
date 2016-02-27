# PlacesAround
Tim nhung dia diem xung quang ban
- Mô tả app
+ App giúp bạn biết vị trí hiện tại của bạn
+ Cho biết danh sách địa điểm gần bạn 
+ Khi chọn vào một địa điểm thì bạn sẽ có thông tin về địa điểm đó như hình ảnh, tên, địa chỉ, khoảng cách tới đó....

- Mô tả cách thức thực hiện app
1. Kết nối các thư viện, cũng nhưng các permission cho Google Place Service 
2. Thiết lập bản đồ bằng MapFragment
3. Tạo GoogleApiClient qua đó ta có thể lấy được currentLocation và thiết lập bản đồ tới vị trí hiện tại
4. Sử dụng PlaceDetectionApi.getCurrentPlace để lấy danh sách địa điểm gần ta
5. Từ đó lấy được name, address, id, location...
6. Để lấy được hình ảnh của một địa điểm ta sử dụng GeoDataApi.getPlacePhotos để lấy danh sách ảnh mà địa điểm đó có

- Yêu cầu để chạy app
Có bật location, và có internet


	