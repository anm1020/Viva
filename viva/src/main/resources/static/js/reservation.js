  const intrId = /*[[${interviewer.intrId}]]*/ null;
  const reservedSlots = /*[[${reservedSlotsJson}]]*/ {};
  const disabledSlots = /*[[${disabledSlotsJson}]]*/ {};

  let selectedDate = null;
  let selectedTime = null;
  let calendarInitialized = false;

  // ✅ 예약 버튼 클릭 → 예약 폼 열기 + 달력 초기화
  $(document).ready(function () {
    $('#btn-open-popup').on('click', function () {
      $('#reservation-box').slideDown();

      if (!calendarInitialized) {
        flatpickr("#calendar", {
          inline: true,
          dateFormat: "Y-m-d",
          minDate: "today",
          onChange: function(_, dateStr) {
            selectedDate = dateStr;

            // 날짜 텍스트 갱신
            const label = document.getElementById('selected-date-label');
            const koreanDate = new Date(dateStr).toLocaleDateString('ko-KR', {
              year: 'numeric', month: 'long', day: 'numeric', weekday: 'short'
            });
            label.textContent = `${koreanDate} 시간 선택`;
            document.getElementById('summary-date').textContent = koreanDate;

            // ✅ Step 이동
            $('#step-time').slideDown();      // 시간선택 보이기
            $('#step-summary').hide();        // 요약 숨기기

            renderTimes();
          }
        });

        calendarInitialized = true;
      }
    });
  });

  // ✅ 시간 선택 버튼 렌더링 함수
  function renderTimes() {
    const grid = $('#times-grid').empty();
    const reserved = reservedSlots[selectedDate] || [];
    const disabled = disabledSlots[selectedDate] || [];
    const blocked = [...reserved, ...disabled];

    for (let h = 9; h <= 18; h++) {
      const t = (h < 10 ? '0' : '') + h + ':00';
      const isDisabled = blocked.includes(t);

      $('<button>')
        .text(t)
        .addClass(isDisabled ? 'disabled' : '')
        .prop('disabled', isDisabled)
        .on('click', function () {
          if (isDisabled) return;

          $('.times-grid button').removeClass('selected');
          $(this).addClass('selected');
          selectedTime = t;

          // ✅ 요약 표시
          document.getElementById('summary-time').textContent = t;
          $('#step-summary').slideDown();
        })
        .appendTo(grid);
    }
  }

  // ✅ 예약 확정 버튼 AJAX
  $(document).on('click', '#btn-confirm', function () {
    if (!selectedDate || !selectedTime) {
      return alert('날짜와 시간을 모두 선택해주세요.');
    }

    const payload = {
      intrId: intrId,
      reservedDate: selectedDate,
      reservedTime: selectedTime
    };

    $.ajax({
      type: 'POST',
      url: '/reservation/save',
      contentType: 'application/json',
      data: JSON.stringify(payload),
      success: function (resId) {
        window.location.href = `/reservation/result?resId=${resId}`;
      },
      error: function (xhr) {
        alert('예약 저장 중 오류: ' + xhr.responseText);
      }
    });
  });
