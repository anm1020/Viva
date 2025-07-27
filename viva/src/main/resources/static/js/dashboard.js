$(".sidebar-link").click(function(e) {
    e.preventDefault();
    const url = $(this).data("url");
    if (url === "/admin/users") {
        $.ajax({
            url: url,
            type: "GET",
            data: { userRole: "mem" }, // ← 반드시!
            success: function(data) { $("#adminMainContent").html(data); },
            error: function() { $("#adminMainContent").html("<p>불러오기 오류!</p>"); }
        });
    } else {
        $.ajax({
            url: url,
            type: "GET",
            success: function(data) { $("#adminMainContent").html(data); },
            error: function() { $("#adminMainContent").html("<p>불러오기 오류!</p>"); }
        });
    }
});

//공지사항 글쓰기 버튼 클릭시 폼 불러오기
$(document).on("click", "#notice-write-btn", function(e) {
    e.preventDefault();
    $.ajax({
        url: "/notice/new",    // GET 방식
        type: "GET",
        success: function(data) {
            $("#adminMainContent").html(data); // 폼으로 본문 영역 교체
        },
        error: function() {
            alert("글쓰기 폼 불러오기 실패!");
        }
    });
});
//"목록으로" 클릭 시 다시 리스트로
$(document).on("click", "#notice-list-btn", function(e) {
    e.preventDefault();
    $.ajax({
        url: "/notice/list",
        type: "GET",
        success: function(data) {
            $("#adminMainContent").html(data);
        },
        error: function() {
            alert("목록 불러오기 실패!");
        }
    });
});
//공지사항 등록 AJAX (폼 submit)
$(document).on("submit", "#noticeForm", function(e) {
    e.preventDefault(); // 기본 submit 막기

    var formData = $(this).serialize();

    $.ajax({
        url: "/notice/new",
        type: "POST",
        data: formData,
        success: function(data) {
            // 등록 후에는 목록 다시 불러오기 (서버에서 redirect 안 해도 됨)
            $("#adminMainContent").html(data);
        },
        error: function() {
            alert("공지 등록 실패!");
        }
    });
});

//공지사항 "수정" 버튼 클릭 시 수정 폼 AJAX로 불러오기
$(document).on("click", ".notice-edit-btn", function(e) {
    e.preventDefault();
    const noticeId = $(this).data("id");
    $.ajax({
        url: "/notice/edit/" + noticeId, // GET
        type: "GET",
        success: function(data) {
            $("#adminMainContent").html(data);
        },
        error: function() {
            alert("수정 폼 불러오기 실패!");
        }
    });
});

// 공지사항 "수정완료" AJAX (edit submit)
$(document).on("submit", "#noticeEditForm", function(e) {
    e.preventDefault(); // submit 막기

    var formData = $(this).serialize();
    var actionUrl = $(this).attr("action"); // form의 action값 그대로 사용

    $.ajax({
        url: actionUrl,
        type: "POST",
        data: formData,
        success: function(data) {
            // 수정 성공 시, 다시 목록을 AJAX로 불러오기
            $.ajax({
                url: "/notice/list",
                type: "GET",
                success: function(listHtml) {
                    $("#adminMainContent").html(listHtml);
                },
                error: function() {
                    alert("목록 다시 불러오기 실패!");
                }
            });
        },
        error: function() {
            alert("공지 수정 실패!");
        }
    });
});