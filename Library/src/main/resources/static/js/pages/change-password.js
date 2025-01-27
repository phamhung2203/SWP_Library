//Change password
//--------------------------------------------------------------------------------------------

$(document).ready(function () {
    $('#change-password-form').on('submit', function (e) {
        e.preventDefault();
        let matKhauUnchecked = $('#matKhau').val();
        let xacNhanMatKhau = $('#xacNhanMatKhau').val();
        if(matKhauUnchecked === xacNhanMatKhau) {
            let url = new URL(window.location.href);
            let params = new URLSearchParams(url.search);
            let last = params.get("auth");
            $.ajax({
                method: 'POST',
                url: '/Library/processforgotpassword?auth=' + last + "&new=" + matKhauUnchecked,
                success: () => {
                    console.log("success");
                    window.location.replace("/Library/login")
                },
                error: (jqXHR, textStatus, errorThrown) => {
                    console.log("Error: " + textStatus + ', ' + errorThrown);
                }
            });
        } else {
            $('#unmatched-message').text("Xác nhận mật khẩu không khớp, vui lòng nhập lại").css('color', 'red');
        }
    });
});

//end change password
//--------------------------------------------------------------------------------------------