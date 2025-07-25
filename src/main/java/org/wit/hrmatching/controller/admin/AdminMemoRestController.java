package org.wit.hrmatching.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wit.hrmatching.service.admin.AdminService;
import org.wit.hrmatching.vo.admin.AdminMemoVO;

import java.util.List;

@RestController
@RequestMapping("/api/admin/memos")
@RequiredArgsConstructor
public class AdminMemoRestController {

    private final AdminService adminService;

    @PostMapping
    public ResponseEntity<Void> createMemo(@RequestBody AdminMemoVO memo) {
        adminService.addMemo(memo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemo(@PathVariable Long id) {
        adminService.removeMemo(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<AdminMemoVO>> getMemos() {
        return ResponseEntity.ok(adminService.listMemos());
    }
}
