package com.uncles.novel.app.jfx.framework.hotkey;

import cn.hutool.core.collection.CollectionUtil;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 快捷键记录器
 *
 * <pre>
 *   if (recorder.record(e)) {
 *     HotKeyCombination combination = recorder.getCombination();
 *     System.out.println("录入热键：" + combination.getText());
 *   }
 *   // label 标签实时显示
 *   text.setText(recorder.getKeyText());
 * </pre>
 *
 * @author blog.unclezs.com
 * @date 2021/03/09 8:30
 */
public class KeyRecorder {
    /**
     * 辅助功能键
     */
    private static final Set<KeyCode> MODIFIERS = CollectionUtil.newHashSet(KeyCode.CONTROL, KeyCode.SHIFT, KeyCode.META, KeyCode.ALT, KeyCode.WINDOWS, KeyCode.UNDEFINED);
    private boolean shift;
    private boolean ctrl;
    private boolean meta;
    private boolean alt;
    /**
     * 功能键
     */
    private KeyCode keyCode;
    /**
     * 快捷键是否合法
     */
    @Getter
    private boolean effective;
    private HotKeyCombination combination;
    @Getter
    private String keyText;

    /**
     * 生成快捷键
     */
    private void generate() {
        if (!effective) {
            return;
        }
        List<KeyCombination.Modifier> combinations = new ArrayList<>();
        if (ctrl) {
            combinations.add(KeyCodeCombination.CONTROL_DOWN);
        }
        if (shift) {
            combinations.add(KeyCodeCombination.SHIFT_DOWN);
        }
        if (meta) {
            combinations.add(KeyCodeCombination.META_DOWN);
        }
        if (alt) {
            combinations.add(KeyCodeCombination.ALT_DOWN);
        }
        this.combination = new HotKeyCombination(keyCode, combinations.toArray(new KeyCombination.Modifier[0]));
    }

    /**
     * 记录快捷键
     * <p>
     * 可以通过getKeyText()方法获取实时的录入结果
     *
     * @param event 键盘事件
     * @return true 则此次录入为合法快捷键
     */
    public boolean record(KeyEvent event) {
        this.combination = null;
        ctrl = event.isControlDown();
        alt = event.isAltDown();
        meta = event.isMetaDown();
        shift = event.isShiftDown();
        StringBuilder text = new StringBuilder();
        if (ctrl) {
            text.append(KeyCode.CONTROL.getName().toLowerCase()).append(" ");
        }
        if (shift) {
            text.append(KeyCode.SHIFT.getName().toLowerCase()).append(" ");
        }
        if (meta) {
            text.append(KeyCode.META.getName().toLowerCase()).append(" ");
        }
        if (alt) {
            text.append(KeyCode.ALT.getName().toLowerCase()).append(" ");
        }
        if (!MODIFIERS.contains(event.getCode())) {
            keyCode = event.getCode();
            effective = true;
            text.append(keyCode.getName().toUpperCase()).append(" ");
        }
        this.keyText = text.toString();
        return effective;
    }

    /**
     * 获取快捷键组合
     *
     * @return 如果不是合法快捷键则返回null
     */
    public HotKeyCombination getCombination() {
        if (!effective) {
            return null;
        }
        if (combination == null) {
            generate();
        }
        return combination;
    }


}