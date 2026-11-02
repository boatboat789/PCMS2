<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/pages/config/meta.jsp"></jsp:include>
<title>Permit Management</title>
<jsp:include page="/WEB-INF/pages/config/css/baseCSS.jsp"></jsp:include>
<style>
.permit-table th, .permit-table td { text-align: center; vertical-align: middle; white-space: nowrap; }
.permit-table td:first-child { text-align: left; font-weight: bold; }
.readonly-note { font-size: 0.8rem; color: #6c757d; }
</style>
</head>
<body>
<jsp:include page="/WEB-INF/pages/config/navbar.jsp"></jsp:include>
<div id="wrapper">
    <div class="row" style="margin:0">
        <div class="col-0 col-xl-1"></div>
        <div class="col-12 col-xl-10" style="padding:20px;">

            <div class="page-header-bar"><h1 class="page-title">Permit Management</h1></div>

            <c:if test="${permit.permitId != 'ADMIN'}">
                <div class="alert alert-warning">คุณอยู่ในโหมดดูอย่างเดียว — เฉพาะ ADMIN เท่านั้นที่แก้ไขได้</div>
            </c:if>

            <div class="content-panel">
            <!-- Tabs -->
            <ul class="nav nav-tabs mb-3" id="pmTabs" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active" id="tab-permits-lnk" data-toggle="tab" href="#tab-permits" role="tab">สิทธิ์ (Permit Definitions)</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" id="tab-users-lnk" data-toggle="tab" href="#tab-users" role="tab">ผู้ใช้ (User Assignment)</a>
                </li>
            </ul>

            <div class="tab-content">

                <!-- Tab 1: Permit Definitions -->
                <div class="tab-pane fade show active" id="tab-permits" role="tabpanel">
                    <p class="text-muted small">แก้ไขสิทธิ์การเข้าถึงในแต่ละ Permit Role — ADMIN ไม่สามารถแก้ไขได้ (ป้องกันล็อกตัวเอง)</p>
                    <div id="permits-loading" class="text-center py-4" aria-live="polite"><i class="fas fa-spinner fa-spin" aria-hidden="true"></i> กำลังโหลด…</div>
                    <div id="permits-content" style="display:none;">
                        <div class="table-responsive">
                            <table class="table table-bordered table-sm permit-table" id="permit-table">
                                <thead class="thead-dark">
                                    <tr>
                                        <th style="min-width:120px">Permit</th>
                                        <th>PCMS<br>Summary</th>
                                        <th>PCMS<br>Detail</th>
                                        <th>→ Prod</th>
                                        <th>→ LBMS</th>
                                        <th>→ QCMS</th>
                                        <th>→ Inspect</th>
                                        <th>→ SFC</th>
                                        <th>Report</th>
                                        <th>User<br>Mgmt</th>
                                        <th>บันทึก</th>
                                    </tr>
                                </thead>
                                <tbody id="permit-tbody"></tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <!-- Tab 2: User Assignment -->
                <div class="tab-pane fade" id="tab-users" role="tabpanel">
                    <p class="text-muted small">กำหนด Permit Role ให้แต่ละ user — คลิกที่ Permit ของ user เพื่อเปลี่ยน</p>
                    <div class="mb-2">
                        <input type="text" id="user-search" class="form-control" placeholder="ค้นหา userId / ชื่อ…" aria-label="ค้นหาผู้ใช้" style="max-width:300px;">
                    </div>
                    <div id="users-loading" class="text-center py-4" aria-live="polite"><i class="fas fa-spinner fa-spin" aria-hidden="true"></i> กำลังโหลด…</div>
                    <div id="users-content" style="display:none;">
                        <div class="table-responsive">
                            <table class="table table-bordered table-sm table-hover" id="user-table">
                                <thead class="thead-dark">
                                    <tr>
                                        <th>UserId</th>
                                        <th>ชื่อ-นามสกุล</th>
                                        <th>แผนก</th>
                                        <th>Permit</th>
                                    </tr>
                                </thead>
                                <tbody id="user-tbody"></tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div><!-- end tab-content -->
            </div><!-- end content-panel -->
        </div>
    </div>
</div>

<!-- Modal: Change User Permit -->
<div class="modal fade" id="modalUserPermit" tabindex="-1">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header"><h5 class="modal-title">เปลี่ยน Permit</h5><button type="button" class="close" data-dismiss="modal" aria-label="ปิด">&times;</button></div>
            <div class="modal-body">
                <p>User: <strong id="modal-userId"></strong></p>
                <div class="form-group">
                    <label>Permit Role</label>
                    <select id="modal-permitId" class="form-control"></select>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                <button class="btn btn-primary" onclick="saveUserPermit()">Save</button>
            </div>
        </div>
    </div>
</div>

<script>
var BASE_URL = ctx + '/Setting/PermitManagement';
var IS_ADMIN = '${permit.permitId}' === 'ADMIN';
var IS_ITSUPP = '${permit.permitId}' === 'ITSUPP';
var CAN_EDIT_USERS = IS_ADMIN || IS_ITSUPP;
var permitOptions = [];
var allUsers = [];

$(document).ready(function() {
    loadPermits();
    $('#tab-users-lnk').on('shown.bs.tab', function() {
        if (allUsers.length === 0) loadUsers();
    });
    $('#user-search').on('input', function() {
        filterUsers($(this).val().toLowerCase());
    });
});

// ──────────────────────────────────────────────
// Tab 1 — Permit Definitions
// ──────────────────────────────────────────────
function loadPermits() {
    $.ajax({
        url: BASE_URL + '/api/permits',
        type: 'GET',
        success: function(res) {
            var list = typeof res === 'string' ? JSON.parse(res) : res;
            permitOptions = list;
            renderPermits(list);
            $('#permits-loading').hide();
            $('#permits-content').show();
        },
        error: function() {
            $('#permits-loading').html('<span class="text-danger">โหลดข้อมูลไม่ได้</span>');
        }
    });
}

var FLAGS = [
    'isPCMSMain', 'isPCMSDetail', 'isPCMSMainToProd', 'isPCMSMainToLBMS',
    'isPCMSMainToQCMS', 'isPCMSMainToInspect', 'isPCMSMainToSFC',
    'isReport', 'isUserManagement'
];

function renderPermits(list) {
    var tbody = $('#permit-tbody').empty();
    list.forEach(function(p) {
        var isAdminRow = p.permitId === 'ADMIN';
        var disabled = !IS_ADMIN || isAdminRow;
        var tr = $('<tr>');
        tr.append($('<td>').text(p.permitId + (p.description ? ' (' + p.description + ')' : '')));
        FLAGS.forEach(function(f) {
            var cb = $('<input type="checkbox">').prop('checked', !!p[f]);
            if (disabled) cb.prop('disabled', true);
            cb.attr('data-permit', p.permitId).attr('data-field', f)
              .attr('aria-label', p.permitId + ' — ' + f);
            tr.append($('<td>').append(cb));
        });
        var btnCell = $('<td>');
        if (!disabled) {
            btnCell.append($('<button class="btn btn-sm btn-primary">บันทึก</button>').on('click', function() {
                savePermit(p.permitId, tr);
            }));
        } else if (isAdminRow) {
            btnCell.append($('<span class="readonly-note">ล็อก</span>'));
        } else {
            btnCell.append($('<span class="readonly-note">ดูอย่างเดียว</span>'));
        }
        tr.append(btnCell);
        tbody.append(tr);
    });
}

function savePermit(permitId, tr) {
    var payload = { permitId: permitId };
    FLAGS.forEach(function(f) {
        payload[f] = tr.find('input[data-field="' + f + '"]').is(':checked');
    });
    Swal.fire({
        title: 'ยืนยัน',
        text: 'บันทึกการเปลี่ยนสิทธิ์ของ ' + permitId + ' ใช่ไหม?',
        icon: 'warning',
        showCancelButton: true,
        cancelButtonText: 'ยกเลิก',
        confirmButtonText: 'ยืนยัน'
    }).then(function(result) {
        if (!result.isConfirmed) return;
        $.ajax({
            url: BASE_URL + '/api/permit/update',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(payload),
            success: function(res) {
                var data = typeof res === 'string' ? JSON.parse(res) : res;
                if (data.status === 'SUCCESS') Swal.fire({ icon: 'success', title: 'สำเร็จ', text: data.message, showConfirmButton: false, timer: 1500 });
                else Swal.fire({ icon: 'error', title: 'เกิดข้อผิดพลาด', text: data.message });
            },
            error: function() { Swal.fire({ icon: 'error', title: 'เกิดข้อผิดพลาด', text: 'ไม่สามารถเชื่อมต่อได้' }); }
        });
    });
}

// ──────────────────────────────────────────────
// Tab 2 — User Assignment
// ──────────────────────────────────────────────
function loadUsers() {
    $.ajax({
        url: BASE_URL + '/api/users',
        type: 'GET',
        success: function(res) {
            allUsers = typeof res === 'string' ? JSON.parse(res) : res;
            renderUsers(allUsers);
            $('#users-loading').hide();
            $('#users-content').show();
        },
        error: function() {
            $('#users-loading').html('<span class="text-danger">โหลดข้อมูลไม่ได้</span>');
        }
    });
}

function renderUsers(list) {
    var tbody = $('#user-tbody').empty();
    list.forEach(function(u) {
        var permitBadge;
        if (CAN_EDIT_USERS) {
            permitBadge = $('<button type="button">').addClass('badge badge-info border-0').text(u.permitId || '(ไม่มี)')
                .on('click', function() { openUserPermitModal(u.userId, u.permitId); });
        } else {
            permitBadge = $('<span>').addClass('badge badge-secondary').text(u.permitId || '(ไม่มี)');
        }
        var tr = $('<tr>');
        tr.append($('<td>').text(u.userId));
        tr.append($('<td>').text((u.firstName || '') + ' ' + (u.lastName || '')));
        tr.append($('<td>').text(u.department || ''));
        tr.append($('<td>').append(permitBadge));
        tbody.append(tr);
    });
}

function filterUsers(keyword) {
    if (!keyword) { renderUsers(allUsers); return; }
    var filtered = allUsers.filter(function(u) {
        return (u.userId || '').toLowerCase().includes(keyword) ||
               (u.firstName || '').toLowerCase().includes(keyword) ||
               (u.lastName || '').toLowerCase().includes(keyword);
    });
    renderUsers(filtered);
}

function openUserPermitModal(userId, currentPermitId) {
    $('#modal-userId').text(userId);
    var sel = $('#modal-permitId').empty();
    sel.append($('<option value="">').text('(ไม่มีสิทธิ์)'));
    permitOptions.forEach(function(p) {
        // ITSUPP กำหนดได้ถึงแค่ระดับตัวเอง ไม่สามารถกำหนด ADMIN ได้
        if (IS_ITSUPP && p.permitId === 'ADMIN') return;
        sel.append($('<option>').val(p.permitId).text(p.permitId + (p.description ? ' — ' + p.description : '')));
    });
    sel.val(currentPermitId || '');
    $('#modalUserPermit').modal('show');
}

function saveUserPermit() {
    var userId = $('#modal-userId').text();
    var permitId = $('#modal-permitId').val();
    $.ajax({
        url: BASE_URL + '/api/user/permit',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ userId: userId, permitId: permitId }),
        success: function(res) {
            var data = typeof res === 'string' ? JSON.parse(res) : res;
            $('#modalUserPermit').modal('hide');
            if (data.status === 'SUCCESS') {
                Swal.fire({ icon: 'success', title: 'สำเร็จ', text: data.message, showConfirmButton: false, timer: 1500 });
                allUsers = [];
                loadUsers();
            } else {
                Swal.fire({ icon: 'error', title: 'เกิดข้อผิดพลาด', text: data.message });
            }
        },
        error: function() { Swal.fire({ icon: 'error', title: 'เกิดข้อผิดพลาด', text: 'ไม่สามารถเชื่อมต่อได้' }); }
    });
}
</script>
</body>
</html>
