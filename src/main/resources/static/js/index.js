fix = function (v) {
    return parseFloat(v.toFixed(2));
}
new Vue({
    el: '#app',
    data: {
        alertMessage: "", //警告信息
        showResult: false, //是否显示结果
        showAlert: false, //是否显示警告
        brands: [], //品牌列表数据
        currentBrandId: 0, //当前选中的品牌的ID
        lightTubeList: [], //T5灯管品类数据
        t5Adapter: {}, //T5连接器信息
        arrString: "", // 需要计算的长度, textarea中接收到的字符串
        arr: [], // 需要计算的长度, 已转换为请求参数
        threshold: 0.1, //默认0.1米的深度
        combinations: [], //返回的组合列表数据
        ids: [], //获取的所有T5的ID
        group: [], // key是id, value是{ name:"", count:0, price:0 }
        adapterCount: 0, //连接器个数
        adapterPrice: 0, //连接器价格
        totalPrice: 0 //总价格
    },
    mounted: function () {
        this.getBrandList();
        this.getLightTubeList();
        this.getT5Adapter();
    },
    watch: {
        currentBrandId: function (val) {
            this.getLightTubeList();
            this.getT5Adapter();
        }
    },
    methods: {
        getBrandList: function () {
            this.post(this,
                "/brandList",
                {},
                function (data, _this) {
                    if (data) {
                        _this.brands = data;
                        _this.currentBrandId = data[0].id;
                    } else {
                        _this.alert("没有品牌数据");
                    }
                });
        },
        getLightTubeList: function () {
            this.post(this,
                "/lightTubeList",
                {brandId: this.currentBrandId},
                function (data, _this) {
                    _this.lightTubeList = data;
                });
        },

        getT5Adapter: function () {
            this.post(this,
                "/t5Adapter",
                {brandId: this.currentBrandId},
                function (data, _this) {
                    _this.t5Adapter = data;
                });
        },
        calculate: function () {
            this.arr = this.arrString.split('\n');
            if (!this.check()) {
                return;
            }
            this.post(this,
                "/calculate",
                {
                    items: this.arr,
                    brandId: this.currentBrandId,
                    threshold: this.threshold
                }, function (data, _this) {
                    _this.showResult = true;
                    _this.ids = [];
                    _this.group = [];
                    _this.combinations = data;
                    _this.adapterCount = 0;
                    _this.adapterPrice = 0;
                    _this.totalPrice = 0;
                    var isFirstTime = true;
                    //取第一个获取属性, 生成列
                    for (var i = 0; i < data.length; i++) {
                        var item = data[i];
                        if (isFirstTime) {
                            for (var key in item.combination) {
                                _this.ids.push(key);
                                var lightTube = _this.lightTubeList[key];
                                if (lightTube) {
                                    _this.group[key] = {};
                                    _this.group[key].name = lightTube.name;
                                    _this.group[key].count = 0;
                                    _this.group[key].price = 0;
                                }
                            }
                            isFirstTime = false;
                        }

                        //开始赋值
                        for (var index in _this.ids) {
                            var id = _this.ids[index];
                            _this.group[id].count = _this.group[id].count + item.combination[id];
                        }
                        _this.adapterCount = _this.adapterCount + item.adapterCount;
                    }

                    _this.adapterPrice = fix(_this.adapterCount * _this.t5Adapter.price);

                    for (var index in _this.ids) {
                        var id = _this.ids[index];
                        _this.group[id].price = fix(_this.group[id].count * _this.lightTubeList[id].price);
                        _this.totalPrice += _this.group[id].price;
                    }
                    _this.totalPrice = fix(_this.totalPrice + _this.adapterPrice);
                });
        },
        reset: function () {
            this.arrString = "";
            this.showResult = false;
        },
        check: function () {
            this.arr = this.arr.filter(function (s) {
                return s && s.trim(); // 注：IE9(不包含IE9)以下的版本没有trim()方法
            });
            if (this.arr == null || this.arr.length < 1) {
                this.alert("请填写需要计算的长度");
                return false;
            }
            for (var i = 0; i < this.arr.length; i++) {
                var item = this.arr[i];
                if (item > 12) {
                    this.alert("长度不能超过12米");
                    return false;
                }
            }
            return true;
        },
        alert: function (v) {
            if (v && v.length > 0) {
                this.showAlert = true;
                scope = this;
                scope.alertMessage = v;
                setTimeout(function () {
                    scope.showAlert = false;
                    scope.alertMessage = "";
                }, 2000)
            }
        },
        post: function (scope, url, param, success, failed) {
            axios({
                method: 'post',
                url: url,
                data: param
            })
                .then(function (resp) {
                    console.log(resp);
                    if (resp.code === 290) {
                        scope.alert(resp.message);
                        if (failed)
                            failed();
                    } else if (success) {
                        success(resp.data.data, scope);
                    }
                })
                .catch(function (e) {
                    scope.showResult = false;
                    if (failed)
                        failed();
                });
        }
    },
})