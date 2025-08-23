// package com.diffchecker.components;

// import javax.swing.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.InputEvent;
// import java.awt.event.KeyEvent;

// public class HotkeyManager {

//   public static void registerHotkeys(JRootPane rootPane, Runnable saveAction, Runnable closeTabAction,
//       Runnable findAction) {
//     InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
//     ActionMap actionMap = rootPane.getActionMap();

//     // New Tab (Ctrl+T)
//     inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK), "newTab");
//     actionMap.put("newTab", new AbstractAction() {
//       @Override
//       public void actionPerformed(ActionEvent e) {
//         addNewTab(tabbedPane);
//       }
//     });

//     // Close Tab (Ctrl+W) -- you already had this inside tabbedPane
//     inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK), "closeTab");
//     actionMap.put("closeTab", new AbstractAction() {
//       @Override
//       public void actionPerformed(ActionEvent e) {
//         int index = tabbedPane.getSelectedIndex();
//         if (index != -1) {
//           Component comp = tabbedPane.getTabComponentAt(index);

//           // 🔒 don’t close the "+" button
//           if (comp instanceof JButton) {
//             return;
//           }

//           tabbedPane.remove(index);

//           if (tabbedPane.getTabCount() == 1) { // only + left
//             onTabEmptyFallback.run();
//           }
//         }
//       }
//     });

//     // Save (Ctrl+S)
//     inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), "save");
//     actionMap.put("save", new AbstractAction() {
//       @Override
//       public void actionPerformed(ActionEvent e) {
//         saveAction.run();
//       }
//     });

//     // Save As (Ctrl+Shift+S)
//     inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK), "saveAs");
//     actionMap.put("saveAs", new AbstractAction() {
//       @Override
//       public void actionPerformed(ActionEvent e) {
//         JOptionPane.showMessageDialog(rootPane, "Save As triggered");
//         // you can hook this to your save-as logic
//       }
//     });

//     // Find (Ctrl+F)
//     inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK), "find");
//     actionMap.put("find", new AbstractAction() {
//       @Override
//       public void actionPerformed(ActionEvent e) {
//         findAction.run();
//       }
//     });
//   }
// }
