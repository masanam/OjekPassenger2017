<?php
$task = filter_input(INPUT_GET, 'task', FILTER_SANITIZE_SPECIAL_CHARS);

$servername = "localhost";
$username = "webtvasi_manager";
$password = "m4n4g3r";
$dbname = "webtvasi_ojekonline";
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
	die("Connection failed: " . $conn->connect_error);
} 
$data=array();
apiAccessRequest($conn);
switch($task)
{
	case "checkConnection":
		$data['result'] = "Success";
		header('Content-Type: application/json');
		echo json_encode($data);
	break;
	case "getDriversAround":
		$sql = "SELECT * FROM drivers";
		$result = $conn->query($sql);
		$data=array();
		if ($result->num_rows > 0) {
		// output data of each row
			$i=0;
			while($row = $result->fetch_object()) {
				$data[$i]['lat'] = $row->lokasisekaranglat;
				$data[$i]['lng'] = $row->lokasisekaranglong;
				$i++;
			}
		}
		header('Content-Type: application/json');
		echo json_encode($data);
	break;
	case "getRate":
		$sql = "SELECT * FROM `settings` where `id`='1'";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
			while($row = $result->fetch_object()) {
				$data['rate'] = $row->ojekrates;
			}
		}
		
		header('Content-Type: application/json');
		echo json_encode($data);
	break;
	case 'userRegister':
		$sql = "SELECT * FROM `user` where `phonenum`='".filter_input(INPUT_POST, 'phoneNum', FILTER_SANITIZE_SPECIAL_CHARS)."'";
		// sqlRecordLog($conn,mysql_escape_string($sql));


		$result = $conn->query($sql);

		// sqlRecordLog($conn,$result->num_rows);

		if ($result->num_rows > 0) {
			$data['response'] = 'USER EXISTS';
			header('Content-Type: application/json');
			echo json_encode($data);
		}else{
			$sql = "insert into `user` (
				`email`,
				`password`,
				`phonenum`
			) values 
			(
				'".filter_input(INPUT_POST, 'email', FILTER_SANITIZE_SPECIAL_CHARS)."',
				'".md5(filter_input(INPUT_POST, 'password', FILTER_SANITIZE_SPECIAL_CHARS))."',
				'".filter_input(INPUT_POST, 'phoneNum', FILTER_SANITIZE_SPECIAL_CHARS)."'
			)";
			$result = $conn->query($sql);

			if ($result) {
				$data['response'] = 'SUCCESS';
				header('Content-Type: application/json');
				echo json_encode($data);
			} else {
				$data['response'] = 'FAILED';
				header('Content-Type: application/json');
				echo json_encode($data);
			}	
		}
		
	break;
	case 'getIdByPhoneNum':
		$sql = "SELECT * FROM `user` where `phonenum`='".filter_input(INPUT_GET, 'phoneNum', FILTER_SANITIZE_SPECIAL_CHARS)."'";
		$result = $conn->query($sql);
		$row = $result->fetch_object();
		if ($result->num_rows > 0) {
			$data['id'] = $row->id;
			header('Content-Type: application/json');
			echo json_encode($data);
		}else
		{
			$data['id'] = 'NULL';
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "loginFront":
		$sql = "SELECT * FROM `user` where `phonenum`='".filter_input(INPUT_GET, 'phonenum', FILTER_SANITIZE_SPECIAL_CHARS)."' and `password`='".md5(filter_input(INPUT_GET, 'password', FILTER_SANITIZE_SPECIAL_CHARS))."'";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
			$data['response'] = 'WELCOME';
			header('Content-Type: application/json');
			echo json_encode($data);
		}else
		{
			$data['response'] = 'FAILED';
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "loginDriver":
		$sql = "SELECT * FROM `drivers` where `drivernik`='".filter_input(INPUT_GET, 'drivernik', FILTER_SANITIZE_SPECIAL_CHARS)."' and `password`='".md5(filter_input(INPUT_GET, 'password', FILTER_SANITIZE_SPECIAL_CHARS))."'";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
			$row = $result->fetch_object();
			$data['response'] = 'WELCOME';
			$data['drivernik'] = $row->drivernik;
			$data['phonenum'] = $row->phonenum;
			$data['drivername'] = $row->drivername;
			$data['lokasisekaranglat'] = $row->lokasisekaranglat;
			$data['lokasisekaranglong'] = $row->lokasisekaranglong;
			$data['sedang_proses_orderid'] = $row->sedang_proses_orderid;
			$data['status'] = $row->status;
			header('Content-Type: application/json');
			echo json_encode($data);
		}else
		{
			$data['response'] = 'FAILED';
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "updateOrder":
		$sql = "UPDATE `order` SET status = 3  where `id`='".filter_input(INPUT_GET, 'orderid', FILTER_SANITIZE_SPECIAL_CHARS)."' order by id DESC";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
			$row = $result->fetch_object();
			$data['response'] = 'WELCOME';
			$data['drivernik'] = $row->drivernik;
			$data['phonenum'] = $row->phonenum;
			$data['drivername'] = $row->drivername;
			$data['lokasisekaranglat'] = $row->lokasisekaranglat;
			$data['lokasisekaranglong'] = $row->lokasisekaranglong;
			$data['sedang_proses_orderid'] = $row->sedang_proses_orderid;
			$data['status'] = $row->status;
			header('Content-Type: application/json');
			echo json_encode($data);
		}else
		{
			$data['response'] = 'FAILED';
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "setOrder":
		$sql = "SELECT * FROM `user` where `phonenum`='".filter_input(INPUT_POST, 'phoneNum', FILTER_SANITIZE_SPECIAL_CHARS)."'";
		$result = $conn->query($sql);
		
		if ($result->num_rows > 0) {
			$row = $result->fetch_object();
			$sql = "insert into `order` (
					`uid`,
					`phoneNum`,
					`from`,
					`to`,
					`distance`,
					`price`,
					`latlangFrom`,
					`latlangTo`,
					`addressfrom`,
					`addressto`,
					`itemtodeliver`,
					`typeorder`,
					`ordertime`,
					`drivereksekusi`,
					`driverid`
				) values 
				(
					'".$row->id."',
					'".filter_input(INPUT_POST, 'phoneNum', FILTER_SANITIZE_SPECIAL_CHARS)."',
					'".filter_input(INPUT_POST, 'from', FILTER_SANITIZE_SPECIAL_CHARS)."',
					'".filter_input(INPUT_POST, 'to', FILTER_SANITIZE_SPECIAL_CHARS)."',
					'".filter_input(INPUT_POST, 'distance', FILTER_SANITIZE_SPECIAL_CHARS)."',
					'".filter_input(INPUT_POST, 'price', FILTER_SANITIZE_SPECIAL_CHARS)."',
					'".filter_input(INPUT_POST, 'latlangFrom', FILTER_SANITIZE_SPECIAL_CHARS)."',
					'".filter_input(INPUT_POST, 'latlangTo', FILTER_SANITIZE_SPECIAL_CHARS)."',
					'".filter_input(INPUT_POST, 'addressfrom', FILTER_SANITIZE_SPECIAL_CHARS)."',
					'".filter_input(INPUT_POST, 'addressto', FILTER_SANITIZE_SPECIAL_CHARS)."',
					'".filter_input(INPUT_POST, 'itemtodeliver', FILTER_SANITIZE_SPECIAL_CHARS)."',
					'".filter_input(INPUT_POST, 'typeorder', FILTER_SANITIZE_SPECIAL_CHARS)."',
					NOW(),
					'NULL',
					'NULL'
				)";	
			sqlRecordLog($conn,mysql_escape_string($sql));

			$result = $conn->query($sql);
			$last_id = $conn->insert_id;

			if ($result) {
				$data['response'] = 'SUCCESS';
				$data['orderid'] = $last_id;
				header('Content-Type: application/json');
				echo json_encode($data);
			} else {
				$data['response'] = 'FAILED';
				header('Content-Type: application/json');
				echo json_encode($data);
			}	
		}else{
			$data['response'] = 'USER NOT EXISTS';
			header('Content-Type: application/json');
			echo json_encode($data);
		}
		
	break;
	case 'getLastOrderStatus':
		$sql = "select * from `order` where `phoneNum`='".filter_input(INPUT_GET, 'phoneNum', FILTER_SANITIZE_SPECIAL_CHARS)."' and status='5' order by id DESC limit 1";	

		$result = $conn->query($sql);

		if ($result) {
			if ($result->num_rows > 0) 
			{
				$row = $result->fetch_object();
				$data['response'] = 'SUCCESS';
				$data['orderType'] = 'Food Delivery';

				$sql = "select * from `drivers` where `sedang_proses_orderid`='".$row->id."' and `status`='1'";	

				$result = $conn->query($sql);

				if ($result) {
					if ($result->num_rows > 0) {
						$row = $result->fetch_object();
						$data['driverName'] = $row->drivername;
						$data['lokasisekaranglat'] = $row->lokasisekaranglat;
						$data['lokasisekaranglong'] = $row->lokasisekaranglong;
						$data['photo'] = $row->photo;
					}
				}
			}
			header('Content-Type: application/json');
			echo json_encode($data);
		} else {
			$data['response'] = 'FAILED';
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "setOrderFood":
		$sql = "insert into `foodorder` (
				`orderid`,
				`foodid`,
				`quantity`,
				`note`,
				`foodname`
			) values 
			(
				'".filter_input(INPUT_POST, 'orderid', FILTER_SANITIZE_SPECIAL_CHARS)."',
				'".filter_input(INPUT_POST, 'foodid', FILTER_SANITIZE_SPECIAL_CHARS)."',
				'".filter_input(INPUT_POST, 'quantity', FILTER_SANITIZE_SPECIAL_CHARS)."',
				'".filter_input(INPUT_POST, 'note', FILTER_SANITIZE_SPECIAL_CHARS)."',
				'".filter_input(INPUT_POST, 'foodname', FILTER_SANITIZE_SPECIAL_CHARS)."'
			)";	
		sqlRecordLog($conn,mysql_escape_string($sql));

		$result = $conn->query($sql);
		$last_id = $conn->insert_id;

		if ($result) {
			$data['response'] = 'SUCCESS';
			$data['orderid'] = $last_id;
			header('Content-Type: application/json');
			echo json_encode($data);
		} else {
			$data['response'] = 'FAILED';
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "getOrderByPhoneNum":
		$sql = "SELECT * FROM order where phoneNum=".filter_input(INPUT_GET, 'phoneNum', FILTER_SANITIZE_SPECIAL_CHARS);
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		// output data of each row
			$i=0;
			while($row = $result->fetch_object()) {
				$data[$i]['ordertime'] = date('d/m/Y H:i:s',strtotime($row->ordertime));
				$data[$i]['from'] = $row->from;
				$data[$i]['to'] = $row->to;
				$data[$i]['latlangFrom'] = $row->latlangFrom;
				$data[$i]['latlangTo'] = $row->latlangTo;
				$data[$i]['addressfrom'] = $row->addressfrom;
				$data[$i]['addressto'] = $row->addressto;
				$data[$i]['distance'] = $row->distance;
				$data[$i]['price'] = $row->price;
				$data[$i]['type'] = $row->type;
				$data[$i]['drivereksekusi'] = $row->type;
				$i++;
			}
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "getBrowseWarung":
		$sql = "SELECT * FROM foodtoko where active='1'";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		// output data of each row
			$i=0;
			while($row = $result->fetch_object()) {
				$data[$i]['pic'] = $row->pic;
				$data[$i]['id'] = $row->id;
				$data[$i]['namawarung'] = $row->namawarung;
				$i++;
			}
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "getBrowseWarungByLatLng":
		$sql = "SELECT *, (3959 * acos (cos ( radians(".filter_input(INPUT_GET, 'lat', FILTER_SANITIZE_SPECIAL_CHARS).") )
      * cos( radians( lat ) )
      * cos( radians( `long` ) - radians(".filter_input(INPUT_GET, 'long', FILTER_SANITIZE_SPECIAL_CHARS).") )
      + sin ( radians(".filter_input(INPUT_GET, 'lat', FILTER_SANITIZE_SPECIAL_CHARS).") )
      * sin( radians( lat ) ) ) ) AS distance FROM foodtoko HAVING distance < '500' and active='1' order by distance";

		// $sql = "SELECT * FROM foodtoko where active='1'";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		// output data of each row
			$i=0;
			while($row = $result->fetch_object()) {
				$data[$i]['pic'] = $row->pic;
				$data[$i]['id'] = $row->id;
				$data[$i]['lat'] = $row->lat;
				$data[$i]['lng'] = $row->long;
				$data[$i]['namawarung'] = $row->namawarung;
				// distance
				$data[$i]['distance'] = round(distance(filter_input(INPUT_GET, 'lat', FILTER_SANITIZE_SPECIAL_CHARS), filter_input(INPUT_GET, 'long', FILTER_SANITIZE_SPECIAL_CHARS), $row->lat, $row->long, 'K'),2);

				$i++;
			}
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "getOrderInProgress":
		$sql = "SELECT * FROM `order` where (`status`='2' or `status`='5') and `phoneNum`='".filter_input(INPUT_GET, 'phoneNum', FILTER_SANITIZE_SPECIAL_CHARS)."' order by id DESC";

		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		// output data of each row
			$i=0;
			while($row = $result->fetch_object()) {
				$data[$i]['typeorder'] = $row->typeorder;
				$data[$i]['uid'] = $row->uid;
				$data[$i]['phoneNum'] = $row->phoneNum;
				$data[$i]['from'] = $row->from;
				$data[$i]['to'] = $row->to;
				$data[$i]['price'] = $row->price;
				$data[$i]['ordertime'] = $row->ordertime;
				$data[$i]['itemtodeliver'] = $row->itemtodeliver;
				$i++;
			}
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "getOrders":
		$sql = "SELECT * FROM `order` where (`status`='2' or `status`='5') order by id DESC";

		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		// output data of each row
			$i=0;
			while($row = $result->fetch_object()) {
				$data[$i]['typeorder'] = $row->typeorder;
				$data[$i]['uid'] = $row->uid;
				$data[$i]['phoneNum'] = $row->phoneNum;
				$data[$i]['addressfrom'] = $row->addressfrom;
				$data[$i]['addressto'] = $row->addressto;
				$data[$i]['from'] = $row->from;
				$data[$i]['to'] = $row->to;
				$data[$i]['price'] = $row->price;
				$data[$i]['ordertime'] = $row->ordertime;
				$data[$i]['itemtodeliver'] = $row->itemtodeliver;
				$data[$i]['distance'] = $row->distance;
				$data[$i]['orderid'] = $row->id;
				$data[$i]['latlangFrom'] = $row->latlangFrom;
				$data[$i]['latlangTo'] = $row->latlangTo;
				$i++;
			}
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "getOrdersById":
		$sql = "SELECT * FROM `order` where `id`='".filter_input(INPUT_GET, 'orderid', FILTER_SANITIZE_SPECIAL_CHARS)."' order by id DESC";

		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		// output data of each row
			$row = $result->fetch_object();
			$data['typeorder'] = $row->typeorder;
			$data['uid'] = $row->uid;
			$data['phoneNum'] = $row->phoneNum;
			$data['addressfrom'] = $row->addressfrom;
			$data['addressto'] = $row->addressto;
			$data['from'] = $row->from;
			$data['to'] = $row->to;
			$data['price'] = $row->price;
			$data['ordertime'] = $row->ordertime;
			$data['itemtodeliver'] = $row->itemtodeliver;
			$data['distance'] = $row->distance;
			$data['orderid'] = $row->id;
			$data['latlangFrom'] = $row->latlangFrom;
			$data['latlangTo'] = $row->latlangTo;
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "getFoodOrderByOrderId":
		$sql = "SELECT * FROM `foodorder` left join food on foodorder.foodid=food.id where `orderid`='".filter_input(INPUT_GET, 'orderid', FILTER_SANITIZE_SPECIAL_CHARS)."' order by foodorder.id DESC";

		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		// output data of each row
			$i=0;
			while($row = $result->fetch_object()) {
				$data[$i]['foodname'] = $row->foodname;
				$data[$i]['price'] = $row->price;
				$data[$i]['quantity'] = $row->quantity;
				$i++;
			}
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "getOrderComplete":
		$sql = "SELECT * FROM order where `status`='1' and `phoneNum`='".filter_input(INPUT_GET, 'phoneNum', FILTER_SANITIZE_SPECIAL_CHARS)."' order by id DESC";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		// output data of each row
			$i=0;
			while($row = $result->fetch_object()) {
				$data[$i]['typeorder'] = $row->typeorder;
				$data[$i]['uid'] = $row->uid;
				$data[$i]['phoneNum'] = $row->phoneNum;
				$data[$i]['from'] = $row->from;
				$data[$i]['to'] = $row->to;
				$data[$i]['price'] = $row->price;
				$data[$i]['ordertime'] = $row->ordertime;
				$data[$i]['itemtodeliver'] = $row->itemtodeliver;
				$data[$i]['drivereksekusi'] = $row->itemtodeliver;
				$data[$i]['driverid'] = $row->itemtodeliver;
				$i++;
			}
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
	case "getListingCategory":
		$sql = "SELECT * FROM food_toko_category where active='1'";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		// output data of each row
			$i=0;
			while($row = $result->fetch_object()) {
				$data[$i]['name'] = $row->name;
				$data[$i]['id'] = $row->id;
				$data[$i]['image'] = $row->image;
				$i++;
			}
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;


	case "getListingFoodByTokoId":
		$sql = "SELECT * FROM food where tokoid='".filter_input(INPUT_GET, 'id', FILTER_SANITIZE_SPECIAL_CHARS)."'";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		// output data of each row
			$i=0;
			while($row = $result->fetch_object()) {
				$data[$i]['foodname'] = $row->foodname;
				$data[$i]['price'] = $row->price;
				$data[$i]['id'] = $row->id;
				$data[$i]['descriptionFood'] = $row->desc;
				$i++;
			}
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;

	case "getTokoDetailsById":
		$sql = "SELECT * FROM foodtoko where id='".filter_input(INPUT_GET, 'id', FILTER_SANITIZE_SPECIAL_CHARS)."'";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		// output data of each row
			$i=0;
			while($row = $result->fetch_object()) {
				$data['namawarung'] = $row->namawarung;
				$data['jambuka'] = $row->jambuka;
				$data['jamtutup'] = $row->jamtutup;
				$data['lat'] = $row->lat;
				$data['lng'] = $row->long;
				$data['desc'] = $row->desc;
				$data['pic'] = $row->pic;
				$data['id'] = $row->id;
				$i++;
			}
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;


	case "getListingCategoryById":
		$sql = "SELECT * FROM food_toko_category where id='".filter_input(INPUT_GET, 'id', FILTER_SANITIZE_SPECIAL_CHARS)."' and active='1'";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) {
		// output data of each row
			$i=0;
			while($row = $result->fetch_object()) {
				$data[$i]['id'] = $row->id;
				$data[$i]['pic'] = $row->pic;
				$data[$i]['namawarung'] = $row->namawarung;
				$i++;
			}
			header('Content-Type: application/json');
			echo json_encode($data);
		}
	break;
}

$conn->close();	

function distance($lat1, $lon1, $lat2, $lon2, $unit) {

  $theta = $lon1 - $lon2;
  $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
  $dist = acos($dist);
  $dist = rad2deg($dist);
  $miles = $dist * 60 * 1.1515;
  $unit = strtoupper($unit);

  if ($unit == "K") {
    return ($miles * 1.609344);
  } else if ($unit == "N") {
      return ($miles * 0.8684);
    } else {
        return $miles;
      }
}

function apiAccessRequest($conn){
	//loginFront
	if($_REQUEST['task'] == 'getOrderInProgress' || $_REQUEST['task'] == 'loginFront'){
		$sql = "insert into `api_access` (
				`request`
			) values 
			(
				'".print_r($_REQUEST,true)."'
			)";
		$conn->query($sql);
	}
}

function sqlRecordLog($conn,$sqli){
	$sql = "insert into `query_log` (
				`querylog`
			) values 
			(
				'".$sqli."'
			)";
	$conn->query($sql);
}
?>