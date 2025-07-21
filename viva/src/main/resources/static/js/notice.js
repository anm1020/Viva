// (1) 사이드바 메뉴 AJAX (공지/회원 등)
$(document).on("click", ".sidebar-link", function(e) {
    e.preventDefault();
    const url = $(this).data("url");
    if (url === "/admin/users") {
        $.ajax({
            url: url,
            type: "GET",
            data: { userRole: "mem" },
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

// (2) 공지사항 상세 펼침
$(document).on("click", ".notice-detail-toggle", function(e){
    e.preventDefault();
    const $row = $(this).closest("tr");
    const noticeId = $(this).data("id");

    // 이미 열려있으면 닫기
    if ($row.next().hasClass("notice-detail-row")) {
        $row.next().remove();
        return;
    }
    $(".notice-detail-row").remove();

    $.ajax({
        url: "/notice/" + noticeId,
        type: "GET",
        success: function(html) {
            const detailTr = `<tr class="notice-detail-row"><td colspan="6">${html}</td></tr>`;
            $row.after(detailTr);
        },
        error: function(){
            alert("상세 불러오기 실패!");
        }
    });
});

// (3) 공지사항 글쓰기 버튼 (폼 AJAX)
$(document).on("click", "#notice-write-btn", function(e) {
    e.preventDefault();
    $.ajax({
        url: "/notice/new",
        type: "GET",
        success: function(data) {
            $("#adminMainContent").html(data);
        },
        error: function() {
            alert("글쓰기 폼 불러오기 실패!");
        }
    });
});

// (4) "목록으로" 버튼
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

// (5) 공지 등록 폼 submit
$(document).on("submit", "#noticeForm", function(e) {
    e.preventDefault();
    var formData = $(this).serialize();
    $.ajax({
        url: "/notice/new",
        type: "POST",
        data: formData,
        success: function(data) {
            $("#adminMainContent").html(data);
        },
        error: function() {
            alert("공지 등록 실패!");
        }
    });
});

// (6) 공지 상세내 "수정" 버튼 (폼 AJAX)
$(document).on("click", ".inline-edit-btn", function(){
    const noticeId = $(this).data("id");
    const $detailTd = $(this).closest("td");
    $.ajax({
        url: "/notice/edit/" + noticeId,
        type: "GET",
        success: function(formHtml){
            $detailTd.html(formHtml);
        },
        error: function(){
            alert("수정 폼 불러오기 실패!");
        }
    });
});

// (7) 공지 수정폼 submit
$(document).on("submit", "#noticeEditForm", function(e) {
    e.preventDefault();
    var formData = $(this).serialize();
    var actionUrl = $(this).attr("action");
    $.ajax({
        url: actionUrl,
        type: "POST",
        data: formData,
        success: function(data) {
            // 수정 성공 시, 전체 목록을 AJAX로 다시 불러옴
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

// (8) 공지 삭제 버튼
$(document).on("click", ".inline-del-btn", function(){
    const noticeId = $(this).data("id");
    if (!confirm("정말 삭제할까요?")) return;

    $.ajax({
        url: "/notice/delete/" + noticeId,
        type: "POST",
        success: function(){
            // 상세 영역 tr 삭제, 원래 목록도 갱신
            $(".notice-detail-row").remove();
            $(`.notice-detail-toggle[data-id="${noticeId}"]`).closest("tr").remove();
        },
        error: function(){
            alert("삭제 실패!");
        }
    });
});