// 테이블 checkbox selectAll하기

    (function () {
    function getItems(master) {
        const table = master.closest('table');
        if (!table) return [];
        const sel =
            master.getAttribute('data-select-all-target') ||
            'tbody input[type="checkbox"]:not([data-skip-selectall])';
        return Array.from(table.querySelectorAll(sel));
    }

    function syncMaster(master) {
    const items = getItems(master).filter(cb => !cb.disabled);
    const total = items.length;
    const checked = items.filter(cb => cb.checked).length;
    master.indeterminate = checked > 0 && checked < total;
    master.checked = total > 0 && checked === total;
}

    function toggleAll(master, checked) {
    getItems(master).forEach(cb => {
    if (cb.disabled) return;
    cb.checked = checked;
    cb.dispatchEvent(new Event('change', { bubbles: true }));
});
    master.indeterminate = false;
}

    // 헤더 마스터 체크박스 → 전체 토글
    document.addEventListener('input', e => {
    if (!e.target.matches('table thead input[type="checkbox"]')) return;
    toggleAll(e.target, e.target.checked);
});

    // 개별 체크 → 마스터 상태 동기화
    document.addEventListener('input', e => {
    if (!e.target.matches('table tbody input[type="checkbox"]:not([data-skip-selectall])')) return;
    const table = e.target.closest('table');
    const master = table && table.querySelector('thead input[type="checkbox"]');
    if (master) syncMaster(master);
});

    // 초기 상태 맞춤
    document.querySelectorAll('table thead input[type="checkbox"]').forEach(syncMaster);
})();
