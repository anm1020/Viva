  // (1) 사이드바 메뉴 AJAX (회원 관리)
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
  
  

//-----------------------------------------------------------------------------------
// (3) 공지사항 글쓰기 버튼 (폼 AJAX)
// 공지사항 글쓰기 버튼 클릭 시 → 글쓰기 폼 비동기 로드
$(document).on('click', '#writeNoticeBtn', function(e) {
  e.preventDefault();

  $('#adminMainContent').load('/notice/new', function(response, status, xhr) {
    if (status !== 'success') {
      $('#adminMainContent').html('<p>글쓰기 폼을 불러오지 못했습니다.</p>');
    }
  });
});

// 2) 작성 폼 등록 버튼 → AJAX POST
$(document).on('submit', '#noticeForm', function(e) {
  e.preventDefault();

  const formData = $(this).serialize(); // 폼 데이터 직렬화

  $.ajax({
    url: '/notice/save',
    type: 'POST',
    data: formData,
    success: function(response) {
      if (response === 'success') {
        alert("공지 등록 완료!");
        // 다시 목록 불러오기
        $('.sidebar-link[data-url="/notice/noticeList"]').click();
      }
    },
    error: function(err) {
      alert("등록 실패");
      console.error(err);
    }
  });
});

// 3) 취소 버튼 → 목록 페이지 비동기 로드
$(document).on('click', '.back-to-noticeList', function(e) {
  e.preventDefault();
  $('#adminMainContent').load('/notice/noticeList');
});

// 공지 제목 누르면 상세 페이지로
$(document).on('click', '.notice-link', function(e) {
  e.preventDefault(); // 브라우저가 직접 이동하지 않도록 막음

  const id = $(this).data('id'); // data-id로부터 ID 추출

    
  $.ajax({
    url: `/notice/detail/${id}`,  // ✅ 절대경로로 요청
    type: 'GET',
    success: function(data) {
      $('#adminMainContent').html(data); // 프래그먼트 응답 삽입
    },
    error: function(err) {
      console.error('상세 요청 실패:', err);
      $('#adminMainContent').html('<p>상세 로드 실패</p>');
    }
  });
});

// 공지 상세에서 '목록으로' 버튼 클릭 시 다시 목록 로드
$(document).on('click', '.btn-notice-list', function(e) {
  e.preventDefault();
  $('#adminMainContent').load('/notice/noticeList', function(resp, status) {
    if (status !== 'success') {
      $('#adminMainContent').html('<p>공지 목록 로드 실패!</p>');
    }
  });
});


// 공지사항 수정 (폼 기반)
function updateNotice() {
  const form = $('#noticeEditForm');
  $.ajax({
    type: "POST",
    url: "/notice/update",
    data: form.serialize(),
    success: function(resp) {
      alert('수정되었습니다!');
      location.reload(); // 또는 상세로 이동
    },
    error: function() {
      alert('수정 실패!');
    }
  });
}

// 공지사항 삭제
function deleteNotice(noticeId) {
  if (!noticeId) {
    alert('삭제할 공지 ID가 없습니다.');
    return;
  }
  if (!confirm('정말 삭제하시겠습니까?')) return;
  $.ajax({
    type: "POST",
    url: "/notice/delete/" + noticeId,
    success: function() {
      alert('삭제되었습니다.');
      // 공지 리스트 부분만 AJAX로 다시 불러오기 (프래그먼트가 아니라면 아래처럼!)
      $('#adminMainContent').load('/notice/noticeList', function(resp, status) {
        if (status !== 'success') {
          $('#adminMainContent').html('<p>공지 목록 로드 실패!</p>');
        }
      });
    },
    error: function() {
      alert('삭제 실패!');
    }
  });
}


// 목록 버튼 클릭 시
$(".btn-notice-list").on("click", function(e) {
  e.preventDefault();
  location.href = "/notice/noticeList";
});

// 만약 버튼에 동적으로 이벤트 부여
$(document).on('click', '#btnUpdateNotice', updateNotice);
$(document).on('click', '#btnDeleteNotice', function() {
  const noticeId = $(this).data('id');
  deleteNotice(noticeId);
});


/*
// 수정 버튼 클릭 시
$("#btnUpdateNotice").on("click", function(e) {
  e.preventDefault();
  if(!confirm("수정하시겠습니까?")) return;

  var formData = $("#noticeEditForm").serialize();
  $.ajax({
    url: "/notice/update",
    type: "POST",
    data: formData,
    success: function(res) {
      if(res === "success") {
        alert("수정되었습니다!");
        location.href = "/notice/noticeList";
      } else {
        alert("실패: " + res);
      }
    },
    error: function(xhr) {
      alert("수정 실패: " + (xhr.responseText || ''));
    }
  });
});

  // 삭제 버튼 클릭 시
  $("#btnDeleteNotice").on("click", function(e) {
    e.preventDefault();
    if(!confirm("정말 삭제하시겠습니까?")) return;

    const noticeId = $(this).data("id");
    $.ajax({
      url: "/notice/delete/" + noticeId,
      type: "POST",
      success: function() {
        alert("삭제되었습니다!");
        location.href = "/notice/noticeList";
      },
      error: function() {
        alert("삭제 실패!");
      }
    });
  }); */



//여기!!!!!!!!!!!!!!!!!!---------------------------------------------------------------------
/*
// 공지사항 수정 (폼 기반)
function updateNotice() {
  const form = $('#noticeEditForm');
  $.ajax({
    type: "POST",
    url: "/notice/update",
    data: form.serialize(),
    success: function(resp) {
      alert('수정되었습니다!');
      location.reload(); // 또는 상세로 이동
    },
    error: function() {
      alert('수정 실패!');
    }
  });
}

// 공지사항 삭제
function deleteNotice(noticeId) {
  if (!noticeId) {
    alert('삭제할 공지 ID가 없습니다.');
    return;
  }
  if (!confirm('정말 삭제하시겠습니까?')) return;
  $.ajax({
    type: "POST",
    url: "/notice/delete/" + noticeId,
    success: function() {
      alert('삭제되었습니다.');
      // 공지 리스트 부분만 AJAX로 다시 불러오기
      $('#adminMainContent').load('/notice/noticeList #adminContent', function(resp, status) {
        if (status !== 'success') {
          $('#adminMainContent').html('<p>공지 목록 로드 실패!</p>');
        }
      });
    },
    error: function() {
      alert('삭제 실패!');
    }
  });
}

/*
// 만약 버튼에 동적으로 이벤트 부여
$(document).on('click', '#btnUpdateNotice', updateNotice);
$(document).on('click', '#btnDeleteNotice', function() {
  const noticeId = $(this).data('id');
  deleteNotice(noticeId);
});
----------여기까지!!!!!!!!!!!!!111-------------------------------------*/
// (4) "목록으로" 버튼 >>  오전에 했던 코드인데 위랑 겹치나 싶어서 일단 주석처리. 01:46
/*$(document).on("click", "#notice-noticeList-btn", function(e) {
    e.preventDefault();
    $.ajax({
        url: "/notice/noticeList",
        type: "GET",
        success: function(data) {
            $("#adminMainContent").html(data);
        },
        error: function() {
            alert("목록 불러오기 실패!");
        }
    });
});*/


// 여기 주석처리함 07.21 01:21
/*// (5) 공지 등록 폼 submit
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
                url: "/notice/noticeList",
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
});*/				//	여기까지 01:21


/*$(function(){
  // 1) 사이드바 “공지관리” 클릭 → 목록(fragment) 로드
  $('.sidebar-link[data-url="/notice/noticeList"]').on('click', function(e){
    e.preventDefault();
    $('#adminMainContent')
      .load('/notice/noticeList :: adminContent');
  });

  // 2) 목록에서 제목 클릭 → 상세(fragment) 로드
  $(document).on('click', '.notice-link', function(e){
    e.preventDefault();
    const url = $(this).attr('href'); // ex: /notice/5
    $('#adminMainContent')
      .load(url + ' :: adminContent');
  });

  // 3) 상세에서 “목록으로” 클릭 → 목록(fragment) 다시 로드
  $(document).on('click', '.back-to-noticeList', function(e){
    e.preventDefault();
    $('#adminMainContent')
      .load('/notice/noticeList :: adminContent');
  });
});*/