// 定义常用的校验，常用的正则 https://www.open-open.com/code/view/1430625516632
layui.define(['jquery'], function (exports) {
    var $ = layui.jquery;
    exports('validate', {
        username: function (value, item) {
            if (!isEmpty(value)) {
                var result = '';
                $.ajax({
                    url: ctx + 'user/check/' + value,
                    data: {
                        "userId": item.getAttribute('id')
                    },
                    async: false,
                    type: 'get',
                    success: function (d) {
                        (!d) && (result = 'Tên người dùng này đã tồn tại')
                    }
                });
                if (!isEmpty(result)) {
                    return result;
                }
            }
        },
        cron: function (value, item) {
            if (!isEmpty(value)) {
                var result = '';
                $.ajax({
                    url: ctx + 'job/cron/check',
                    data: {
                        "cron": value
                    },
                    async: false,
                    type: 'get',
                    success: function (d) {
                        (!d) && (result = 'biểu thức cron là bất hợp pháp')
                    }
                });
                if (!isEmpty(result)) {
                    return result;
                }
            }
        },
        email: function (value) {
            if (!isEmpty(value)) {
                if (!new RegExp("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$").test(value)) {
                    return 'Vui lòng điền đúng địa chỉ email';
                }
            }
        },
        phone: function (value) {
            if (!isEmpty(value)) {
                if (!new RegExp("^1\\d{10}$").test(value)) {
                    return 'Vui lòng điền đúng số điện thoại di động';
                }
            }
        },
        number: function (value) {
            if (!isEmpty(value)) {
                if (!new RegExp("^[0-9]*$").test(value)) {
                    return 'Chỉ có thể điền số';
                }
            }
        },
        range: function (value, item) {
            var minlength = item.getAttribute('minlength') ? item.getAttribute('minlength') : -1;
            var maxlength = item.getAttribute('maxlength') ? item.getAttribute('maxlength') : -1;
            var length = value.length;
            if (minlength === -1) {
                if (length > maxlength) {
                    return 'Chiều dài không thể vượt quá ' + maxlength + ' nhân vật';
                }
            } else if (maxlength === -1) {
                if (length < minlength) {
                    return 'Chiều dài không thể nhỏ hơn ' + minlength + ' nhân vật';
                }
            } else {
                if (length > maxlength || length < minlength) {
                    return 'Phạm vi chiều dài ' + minlength + ' ~ ' + maxlength + ' nhân vật';
                }
            }
        }
    });

    function isEmpty(obj) {
        return typeof obj == 'undefined' || obj == null || obj === '';
    }
});