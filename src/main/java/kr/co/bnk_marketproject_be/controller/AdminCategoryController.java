package kr.co.bnk_marketproject_be.controller;

import kr.co.bnk_marketproject_be.dto.AdminCategoryDTO;
import kr.co.bnk_marketproject_be.service.AdminCategoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryService service;

    // ====== View ======
    @GetMapping("/admin/admin_category")
    public String adminCategoryPage(Model model) {
        // 필요시 서버 렌더링용 데이터 세팅
        model.addAttribute("categories", service.listAll());
        return "admin/admin_category";
    }

    // ====== API ======
    @GetMapping("/api/admin/categories")
    @ResponseBody
    public List<AdminCategoryDTO> list() {
        return service.listAll();
    }


    // 1차 카테고리 추가 (parentId=null)
    @PostMapping("/api/admin/categories/parent")
    @ResponseBody
    public Map<String, Object> addParent(@RequestBody Map<String, String> body) {
        String name = body.getOrDefault("name", "").trim();
        service.addParentCategory(name);
        return Map.of("ok", true);
    }

    // 2차 카테고리 추가
    @PostMapping("/api/admin/categories/child")
    @ResponseBody
    public Map<String, Object> addChild(@RequestBody Map<String, String> body) {
        Integer parentId = Integer.valueOf(body.get("parentId"));
        String  name     = body.getOrDefault("name", "").trim();
        service.addChildCategory(parentId, name);
        return Map.of("ok", true);
    }

    // 카테고리 삭제 (1차면 하위 전부 같이 삭제)
    @DeleteMapping("/api/admin/categories/{id}")
    @ResponseBody
    public Map<String, Object> delete(@PathVariable Integer id) {
        service.deleteCategory(id);
        return Map.of("ok", true);
    }

    @DeleteMapping("/api/admin/categories/{id}/children")
    @ResponseBody
    public Map<String, Object> deleteChildren(@PathVariable Integer id) {
        service.deleteChildrenOnly(id);
        return Map.of("ok", true);
    }


    // ✅ 전체 커밋 (삭제 + 수정)
    // PUT, POST 둘 다 허용하도록 두 개의 매핑을 둔다
    @PutMapping("/api/admin/categories/commit")
    @ResponseBody
    public Map<String, Object> commit(@RequestBody CategoryCommitRequest req) {
        service.deleteAll(req.getDeletedIds());    // 1) 삭제
        service.updateAll(req.getCategories());    // 2) 생성/수정
        return Map.of("ok", true);
    }

    @PostMapping("/api/admin/categories/commit")
    @ResponseBody
    public Map<String, Object> commitPost(@RequestBody CategoryCommitRequest req) {
        // POST 요청으로 들어와도 동일한 로직 실행
        return commit(req);
    }

    // ✅ 요청 DTO (내부 static class)
    @Getter
    @Setter
    public static class CategoryCommitRequest {
        private List<Integer> deletedIds;
        private List<AdminCategoryDTO> categories;
    }
}
