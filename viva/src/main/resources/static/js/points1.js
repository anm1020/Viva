// src/main/resources/static/js/points1.js
$(function() {
  // 1) 사이드바 “포인트관리” 클릭 시 첫 페이지(0) 로드
  $(document).on('click', '.sidebar-link[data-url="/admin/points"]', function(e) {
    e.preventDefault();
    loadPage(0);
  });

  // 2) 페이징 링크 클릭
  $(document).on('click', '.page-btn', function(e) {
    e.preventDefault();
    const page = $(this).data('page');
    if (typeof page !== 'undefined' && page >= 0) loadPage(page);
  });


  // 3) 승인/거절 버튼 클릭
  $(document).on('click', '.btn-approve, .btn-reject', function(e) {
    e.preventDefault();
    const isApprove = $(this).hasClass('btn-approve');
    if (!confirm(isApprove ? '정말 승인하시겠습니까?' : '정말 거절하시겠습니까?')) return;

    const id  = $(this).data('id');
    const url = isApprove
      ? '/admin/approve/' + id
      : '/admin/reject/'  + id;

    $.post(url)
      .done(function(msg) {
        alert(msg);
        // 상태 컬럼만 업데이트 (버튼은 그대로)
        const statusHtml = isApprove
          ? '<span class="status-approved">승인</span>'
          : '<span class="status-rejected">거절</span>';
        $('#row-' + id)
          .find('td.status-cell')
          .html(statusHtml);
      })
      .fail(function(xhr) {
        alert((isApprove ? '승인' : '거절') + ' 실패: ' + xhr.responseText);
      });
  });

  // ▶ 페이지 로드 헬퍼
  function loadPage(page) {
    $('#adminMainContent')
      .html('<p>로딩 중…</p>')
      .load('/admin/points?page=' + page);
  }
});
