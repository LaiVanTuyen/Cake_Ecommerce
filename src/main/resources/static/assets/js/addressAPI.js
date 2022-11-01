// api address
const BASE_API_URL = 'https://provinces.open-api.vn/api'
fetch(`${BASE_API_URL}/p/`)
    .then(Response => Response.json())
    .then(data => addProvinces(data))

function addProvinces(provinces){
    var provinceSelect = document.getElementById('province-select');
    provinces.forEach(province => {
        var option = document.createElement("option");
        option.text = province.name;
        option.value = province.code + '-' + province.name;
        provinceSelect.add(option);
    });

    var provinceSelect1 = document.getElementById('province-select-1');
    provinces.forEach(province => {
        var option = document.createElement("option");
        option.text = province.name;
        option.value = province.code + '-' + province.name;
        provinceSelect1.add(option);
    });
}

// add district
function addDistricts(value){
    var code = value.split('-')[0];
    removeOption(document.getElementById('district-select'));
    removeOption(document.getElementById('ward-select'));
    var districtSelect = document.getElementById('district-select');
    fetch(`${BASE_API_URL}/p/${code}/?depth=2`)
        .then(Response => Response.json())
        .then(data => data.districts)
        .then(districts => {
            districts.forEach(district => {
                var option = document.createElement("option");
                option.value = district.code + '-' +district.name;
                option.text = district.name;
                districtSelect.add(option);
            });
        })

    removeOption(document.getElementById('district-select-1'));
    removeOption(document.getElementById('ward-select-1'));
    var districtSelect1 = document.getElementById('district-select-1');
    fetch(`${BASE_API_URL}/p/${code}/?depth=2`)
        .then(Response => Response.json())
        .then(data => data.districts)
        .then(districts => {
            districts.forEach(district => {
                var option = document.createElement("option");
                option.value = district.code + '-' +district.name;
                option.text = district.name;
                districtSelect1.add(option);
            });
        })
}

// add wards
function addWards(value){
    var code = value.split('-')[0];
    removeOption(document.getElementById('ward-select'));
    var wardDistrict = document.getElementById('ward-select');
    fetch(`${BASE_API_URL}/d/${code}/?depth=2`)
        .then(Response => Response.json())
        .then(data => data.wards)
        .then(wards => {
            wards.forEach(ward => {
                var option = document.createElement("option");
                option.value = ward.name;
                option.text = ward.name;
                wardDistrict.add(option);
            });
        })

    removeOption(document.getElementById('ward-select-1'));
    var wardDistrict1 = document.getElementById('ward-select-1');
    fetch(`${BASE_API_URL}/d/${code}/?depth=2`)
        .then(Response => Response.json())
        .then(data => data.wards)
        .then(wards => {
            wards.forEach(ward => {
                var option = document.createElement("option");
                option.value = ward.name;
                option.text = ward.name;
                wardDistrict1.add(option);
            });
        })
}

// remove option
function removeOption(obj){
    while(obj.length > 1){
        obj.remove(1);
    }
}

// end api address