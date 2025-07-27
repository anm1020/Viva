// 관리자 사이드바에서 게시글관리 메뉴 클릭 시
$(document).on("click", ".sidebar-link[data-url='/admin/boardList']", function(e) {
    e.preventDefault();
    loadBoardList(0);
});

// 게시글 리스트 AJAX 불러오기
function loadBoardList(page) {
    $.get("/admin/boardList", {page: page || 0}, function(fragment) {
        $("#adminMainContent").html(fragment);
    });
}

// 게시글 상세 AJAX
$(document).on("click", ".board-link", function(e) {
    e.preventDefault();
    const id = $(this).data("id");
    $.get("/admin/boardList/" + id, function(fragment) {
        $("#adminMainContent").html(fragment);
    });
});

// 게시글 삭제 AJAX (목록, 상세에서 공통)
$(document).on("click", ".btn-delete-board", function(e) {
    e.preventDefault();
    if (!confirm("정말 삭제할까요?")) return;
    const id = $(this).data("id");
    $.post("/admin/boardList/delete/" + id, function(res) {
        if (res === "success") {
            alert("삭제되었습니다.");
            loadBoardList(0);
        }
    });
});

// 상세 > 목록 돌아가기
$(document).on("click", ".btn-back-boardlist", function(e) {
    e.preventDefault();
    loadBoardList(0);
});

// 페이징 이동
$(document).on("click", ".page-link", function(e) {
    e.preventDefault();
    const page = $(this).data("page");
    loadBoardList(page);
});
