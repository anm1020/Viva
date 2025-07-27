$(document).on('change', 'input[name="userRole"]', function() {
    var selectedRole = $('input[name="userRole"]:checked').val();
    console.log("selectedRole:", selectedRole);
    $.ajax({
        url: "/admin/users",
        type: "GET",
        data: { userRole: selectedRole },
        success: function(data){
            $("#adminMainContent").html(data);
        },
        error: function(){
            alert('회원목록 불러오기 실패!');
        }
    });
});

$(document).on('click', '.toggleStatusBtn', function() {
    let button = $(this);
    let userId = button.data('user-id');
    let currentType = button.data('current-type');
    
    // 상태 토글 로직
    let newType = currentType === 'Y' ? 'N' : 'Y';
    
    $.ajax({
        url: '/admin/users/toggleStatus',
        method: 'POST',
        data: JSON.stringify({ userId: userId, userType: newType }),
        contentType: 'application/json',
		success: function(response) {
		    if(response.result === 'success') {
		        button.data('current-type', newType);
		        button.text(newType === 'Y' ? 'ACTIVE' : 'INACTIVE');
		        button.toggleClass('active inactive');
		        // ✅ 상태변경 성공 알림!
		        alert('상태가 변경되었습니다.');
		    } else {
		        alert('상태 변경에 실패했습니다.');
		    }
		},
    });
});
// 권한 버튼 토글 (면접관만)
$(document).on('click', '.toggleOuthBtn', function() {
    let button = $(this);
    let userId = button.data('user-id');
    let currentOuth = button.data('user-outh');
    let newOuth = currentOuth === 'Y' ? 'N' : 'Y'; // 토글

    $.ajax({
        url: '/admin/users/toggleOuth',
        method: 'POST',
        data: JSON.stringify({ userId: userId, userOuth: newOuth }),
        contentType: 'application/json',
        success: function(response) {
            if(response.result === 'success') {
                button.data('user-outh', newOuth);
                button.text(newOuth === 'Y' ? '승인' : '미승인');
                button.toggleClass('outh-active outh-inactive');
                alert('권한 상태가 변경되었습니다.');
            } else {
                alert('권한 상태 변경에 실패했습니다.');
            }
        },
        error: function() {
            alert('서버 오류가 발생했습니다.');
        }
    });
});

// 페이징 링크 클릭 → AJAX로 본문 갱신!
$(document).on('click', '.pagination a', function(e) {
    e.preventDefault();
    const url = $(this).attr('href');
    $.ajax({
        url: url,
        type: 'GET',
        success: function(data){
            $('#adminMainContent').html(data);
        },
        error: function(){
            alert('페이지 불러오기 실패!');
        }
    });
});
