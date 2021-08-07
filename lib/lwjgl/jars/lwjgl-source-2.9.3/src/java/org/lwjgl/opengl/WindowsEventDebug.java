package org.lwjgl.opengl;

import org.lwjgl.LWJGLUtil;

final class WindowsEventDebug {

	private WindowsEventDebug() {
	}

	static int printMessage(String msg, long wParam, long lParam) {
		System.out.println(msg + ": 0x" + Long.toHexString(wParam).toUpperCase() + " | " + Long.toHexString(lParam).toUpperCase());
		return 0;
	}

	static int printMessage(int msg, long wParam, long lParam) {
		System.out.print(LWJGLUtil.toHexString(msg) + ": ");
		switch ( msg ) {
			case 0x0000:
				return printMessage("WM_NULL", wParam, lParam);
			case 0x0001:
				return printMessage("WM_CREATE", wParam, lParam);
			case 0x0002:
				return printMessage("WM_DESTROY", wParam, lParam);
			case 0x0003:
				return printMessage("WM_MOVE", wParam, lParam);
			case 0x0005:
				return printMessage("WM_SIZE", wParam, lParam);
			case 0x0006:
				return printMessage("WM_ACTIVATE", wParam, lParam);
			case 0x0007:
				return printMessage("WM_SETFOCUS", wParam, lParam);
			case 0x0008:
				return printMessage("WM_KILLFOCUS", wParam, lParam);
			case 0x000A:
				return printMessage("WM_ENABLE", wParam, lParam);
			case 0x000B:
				return printMessage("WM_SETREDRAW", wParam, lParam);
			case 0x000C:
				return printMessage("WM_SETTEXT", wParam, lParam);
			case 0x000D:
				return printMessage("WM_GETTEXT", wParam, lParam);
			case 0x000E:
				return printMessage("WM_GETTEXTLENGTH", wParam, lParam);
			case 0x000F:
				return printMessage("WM_PAINT", wParam, lParam);
			case 0x0010:
				return printMessage("WM_CLOSE", wParam, lParam);
			case 0x0011:
				return printMessage("WM_QUERYENDSESSION", wParam, lParam);
			case 0x0013:
				return printMessage("WM_QUERYOPEN", wParam, lParam);
			case 0x0016:
				return printMessage("WM_ENDSESSION", wParam, lParam);
			case 0x0012:
				return printMessage("WM_QUIT", wParam, lParam);
			case 0x0014:
				return printMessage("WM_ERASEBKGND", wParam, lParam);
			case 0x0015:
				return printMessage("WM_SYSCOLORCHANGE", wParam, lParam);
			case 0x0018:
				return printMessage("WM_SHOWWINDOW", wParam, lParam);
			case 0x001A:
				return printMessage("WM_WININICHANGE", wParam, lParam);
			case 0x001B:
				return printMessage("WM_DEVMODECHANGE", wParam, lParam);
			case 0x001C:
				return printMessage("WM_ACTIVATEAPP", wParam, lParam);
			case 0x001D:
				return printMessage("WM_FONTCHANGE", wParam, lParam);
			case 0x001E:
				return printMessage("WM_TIMECHANGE", wParam, lParam);
			case 0x001F:
				return printMessage("WM_CANCELMODE", wParam, lParam);
			case 0x0020:
				return printMessage("WM_SETCURSOR", wParam, lParam);
			case 0x0021:
				return printMessage("WM_MOUSEACTIVATE", wParam, lParam);
			case 0x0022:
				return printMessage("WM_CHILDACTIVATE", wParam, lParam);
			case 0x0023:
				return printMessage("WM_QUEUESYNC", wParam, lParam);
			case 0x0024:
				return printMessage("WM_GETMINMAXINFO", wParam, lParam);
			case 0x0026:
				return printMessage("WM_PAINTICON", wParam, lParam);
			case 0x0027:
				return printMessage("WM_ICONERASEBKGND", wParam, lParam);
			case 0x0028:
				return printMessage("WM_NEXTDLGCTL", wParam, lParam);
			case 0x002A:
				return printMessage("WM_SPOOLERSTATUS", wParam, lParam);
			case 0x002B:
				return printMessage("WM_DRAWITEM", wParam, lParam);
			case 0x002C:
				return printMessage("WM_MEASUREITEM", wParam, lParam);
			case 0x002D:
				return printMessage("WM_DELETEITEM", wParam, lParam);
			case 0x002E:
				return printMessage("WM_VKEYTOITEM", wParam, lParam);
			case 0x002F:
				return printMessage("WM_CHARTOITEM", wParam, lParam);
			case 0x0030:
				return printMessage("WM_SETFONT", wParam, lParam);
			case 0x0031:
				return printMessage("WM_GETFONT", wParam, lParam);
			case 0x0032:
				return printMessage("WM_SETHOTKEY", wParam, lParam);
			case 0x0033:
				return printMessage("WM_GETHOTKEY", wParam, lParam);
			case 0x0037:
				return printMessage("WM_QUERYDRAGICON", wParam, lParam);
			case 0x0039:
				return printMessage("WM_COMPAREITEM", wParam, lParam);
			case 0x003D:
				return printMessage("WM_GETOBJECT", wParam, lParam);
			case 0x0041:
				return printMessage("WM_COMPACTING", wParam, lParam);
			case 0x0044:
				return printMessage("WM_COMMNOTIFY", wParam, lParam);
			case 0x0046:
				return printMessage("WM_WINDOWPOSCHANGING", wParam, lParam);
			case 0x0047:
				return printMessage("WM_WINDOWPOSCHANGED", wParam, lParam);
			case 0x0048:
				return printMessage("WM_POWER", wParam, lParam);
			case 0x004A:
				return printMessage("WM_COPYDATA", wParam, lParam);
			case 0x004B:
				return printMessage("WM_CANCELJOURNAL", wParam, lParam);
			case 0x004E:
				return printMessage("WM_NOTIFY", wParam, lParam);
			case 0x0050:
				return printMessage("WM_INPUTLANGCHANGEREQUEST", wParam, lParam);
			case 0x0051:
				return printMessage("WM_INPUTLANGCHANGE", wParam, lParam);
			case 0x0052:
				return printMessage("WM_TCARD", wParam, lParam);
			case 0x0053:
				return printMessage("WM_HELP", wParam, lParam);
			case 0x0054:
				return printMessage("WM_USERCHANGED", wParam, lParam);
			case 0x0055:
				return printMessage("WM_NOTIFYFORMAT", wParam, lParam);
			case 0x007B:
				return printMessage("WM_CONTEXTMENU", wParam, lParam);
			case 0x007C:
				return printMessage("WM_STYLECHANGING", wParam, lParam);
			case 0x007D:
				return printMessage("WM_STYLECHANGED", wParam, lParam);
			case 0x007E:
				return printMessage("WM_DISPLAYCHANGE", wParam, lParam);
			case 0x007F:
				return printMessage("WM_GETICON", wParam, lParam);
			case 0x0080:
				return printMessage("WM_SETICON", wParam, lParam);
			case 0x0081:
				return printMessage("WM_NCCREATE", wParam, lParam);
			case 0x0082:
				return printMessage("WM_NCDESTROY", wParam, lParam);
			case 0x0083:
				return printMessage("WM_NCCALCSIZE", wParam, lParam);
			case 0x0084:
				return printMessage("WM_NCHITTEST", wParam, lParam);
			case 0x0085:
				return printMessage("WM_NCPAINT", wParam, lParam);
			case 0x0086:
				return printMessage("WM_NCACTIVATE", wParam, lParam);
			case 0x0087:
				return printMessage("WM_GETDLGCODE", wParam, lParam);
			case 0x0088:
				return printMessage("WM_SYNCPAINT", wParam, lParam);
			case 0x00A0:
				return printMessage("WM_NCMOUSEMOVE", wParam, lParam);
			case 0x00A1:
				return printMessage("WM_NCLBUTTONDOWN", wParam, lParam);
			case 0x00A2:
				return printMessage("WM_NCLBUTTONUP", wParam, lParam);
			case 0x00A3:
				return printMessage("WM_NCLBUTTONDBLCLK", wParam, lParam);
			case 0x00A4:
				return printMessage("WM_NCRBUTTONDOWN", wParam, lParam);
			case 0x00A5:
				return printMessage("WM_NCRBUTTONUP", wParam, lParam);
			case 0x00A6:
				return printMessage("WM_NCRBUTTONDBLCLK", wParam, lParam);
			case 0x00A7:
				return printMessage("WM_NCMBUTTONDOWN", wParam, lParam);
			case 0x00A8:
				return printMessage("WM_NCMBUTTONUP", wParam, lParam);
			case 0x00A9:
				return printMessage("WM_NCMBUTTONDBLCLK", wParam, lParam);
			case 0x00AB:
				return printMessage("WM_NCXBUTTONDOWN", wParam, lParam);
			case 0x00AC:
				return printMessage("WM_NCXBUTTONUP", wParam, lParam);
			case 0x00AD:
				return printMessage("WM_NCXBUTTONDBLCLK", wParam, lParam);
			case 0x00FE:
				return printMessage("WM_INPUT_DEVICE_CHANGE", wParam, lParam);
			case 0x00FF:
				return printMessage("WM_INPUT", wParam, lParam);
			case 0x0100:
				return printMessage("WM_KEYDOWN", wParam, lParam);
			case 0x0101:
				return printMessage("WM_KEYUP", wParam, lParam);
			case 0x0102:
				return printMessage("WM_CHAR", wParam, lParam);
			case 0x0103:
				return printMessage("WM_DEADCHAR", wParam, lParam);
			case 0x0104:
				return printMessage("WM_SYSKEYDOWN", wParam, lParam);
			case 0x0105:
				return printMessage("WM_SYSKEYUP", wParam, lParam);
			case 0x0106:
				return printMessage("WM_SYSCHAR", wParam, lParam);
			case 0x0107:
				return printMessage("WM_SYSDEADCHAR", wParam, lParam);
			case 0x0109:
				return printMessage("WM_UNICHAR", wParam, lParam);
			case 0xFFFF:
				return printMessage("UNICODE_NOCHAR", wParam, lParam);
			case 0x0108:
				return printMessage("WM_KEYLAST", wParam, lParam);
			case 0x010D:
				return printMessage("WM_IME_STARTCOMPOSITION", wParam, lParam);
			case 0x010E:
				return printMessage("WM_IME_ENDCOMPOSITION", wParam, lParam);
			case 0x010F:
				return printMessage("WM_IME_COMPOSITION", wParam, lParam);
			case 0x0110:
				return printMessage("WM_INITDIALOG", wParam, lParam);
			case 0x0111:
				return printMessage("WM_COMMAND", wParam, lParam);
			case 0x0112:
				return printMessage("WM_SYSCOMMAND", wParam, lParam);
			case 0x0113:
				return printMessage("WM_TIMER", wParam, lParam);
			case 0x0114:
				return printMessage("WM_HSCROLL", wParam, lParam);
			case 0x0115:
				return printMessage("WM_VSCROLL", wParam, lParam);
			case 0x0116:
				return printMessage("WM_INITMENU", wParam, lParam);
			case 0x0117:
				return printMessage("WM_INITMENUPOPUP", wParam, lParam);
			case 0x0119:
				return printMessage("WM_GESTURE", wParam, lParam);
			case 0x011A:
				return printMessage("WM_GESTURENOTIFY", wParam, lParam);
			case 0x011F:
				return printMessage("WM_MENUSELECT", wParam, lParam);
			case 0x0120:
				return printMessage("WM_MENUCHAR", wParam, lParam);
			case 0x0121:
				return printMessage("WM_ENTERIDLE", wParam, lParam);
			case 0x0122:
				return printMessage("WM_MENURBUTTONUP", wParam, lParam);
			case 0x0123:
				return printMessage("WM_MENUDRAG", wParam, lParam);
			case 0x0124:
				return printMessage("WM_MENUGETOBJECT", wParam, lParam);
			case 0x0125:
				return printMessage("WM_UNINITMENUPOPUP", wParam, lParam);
			case 0x0126:
				return printMessage("WM_MENUCOMMAND", wParam, lParam);
			case 0x0127:
				return printMessage("WM_CHANGEUISTATE", wParam, lParam);
			case 0x0128:
				return printMessage("WM_UPDATEUISTATE", wParam, lParam);
			case 0x0129:
				return printMessage("WM_QUERYUISTATE", wParam, lParam);
			case 0x0132:
				return printMessage("WM_CTLCOLORMSGBOX", wParam, lParam);
			case 0x0133:
				return printMessage("WM_CTLCOLOREDIT", wParam, lParam);
			case 0x0134:
				return printMessage("WM_CTLCOLORLISTBOX", wParam, lParam);
			case 0x0135:
				return printMessage("WM_CTLCOLORBTN", wParam, lParam);
			case 0x0136:
				return printMessage("WM_CTLCOLORDLG", wParam, lParam);
			case 0x0137:
				return printMessage("WM_CTLCOLORSCROLLBAR", wParam, lParam);
			case 0x0138:
				return printMessage("WM_CTLCOLORSTATIC", wParam, lParam);
			case 0x01E1:
				return printMessage("MN_GETHMENU", wParam, lParam);
			case 0x0200:
				return printMessage("WM_MOUSEMOVE", wParam, lParam);
			case 0x0201:
				return printMessage("WM_LBUTTONDOWN", wParam, lParam);
			case 0x0202:
				return printMessage("WM_LBUTTONUP", wParam, lParam);
			case 0x0203:
				return printMessage("WM_LBUTTONDBLCLK", wParam, lParam);
			case 0x0204:
				return printMessage("WM_RBUTTONDOWN", wParam, lParam);
			case 0x0205:
				return printMessage("WM_RBUTTONUP", wParam, lParam);
			case 0x0206:
				return printMessage("WM_RBUTTONDBLCLK", wParam, lParam);
			case 0x0207:
				return printMessage("WM_MBUTTONDOWN", wParam, lParam);
			case 0x0208:
				return printMessage("WM_MBUTTONUP", wParam, lParam);
			case 0x0209:
				return printMessage("WM_MBUTTONDBLCLK", wParam, lParam);
			case 0x020A:
				return printMessage("WM_MOUSEWHEEL", wParam, lParam);
			case 0x020B:
				return printMessage("WM_XBUTTONDOWN", wParam, lParam);
			case 0x020C:
				return printMessage("WM_XBUTTONUP", wParam, lParam);
			case 0x020D:
				return printMessage("WM_XBUTTONDBLCLK", wParam, lParam);
			case 0x020E:
				return printMessage("WM_MOUSEHWHEEL", wParam, lParam);
			case 0x0210:
				return printMessage("WM_PARENTNOTIFY", wParam, lParam);
			case 0x0211:
				return printMessage("WM_ENTERMENULOOP", wParam, lParam);
			case 0x0212:
				return printMessage("WM_EXITMENULOOP", wParam, lParam);
			case 0x0213:
				return printMessage("WM_NEXTMENU", wParam, lParam);
			case 0x0214:
				return printMessage("WM_SIZING", wParam, lParam);
			case 0x0215:
				return printMessage("WM_CAPTURECHANGED", wParam, lParam);
			case 0x0216:
				return printMessage("WM_MOVING", wParam, lParam);
			case 0x0218:
				return printMessage("WM_POWERBROADCAST", wParam, lParam);
			case 0x8013:
				return printMessage("PBT_POWERSETTINGCHANGE", wParam, lParam);
			case 0x0219:
				return printMessage("WM_DEVICECHANGE", wParam, lParam);
			case 0x0220:
				return printMessage("WM_MDICREATE", wParam, lParam);
			case 0x0221:
				return printMessage("WM_MDIDESTROY", wParam, lParam);
			case 0x0222:
				return printMessage("WM_MDIACTIVATE", wParam, lParam);
			case 0x0223:
				return printMessage("WM_MDIRESTORE", wParam, lParam);
			case 0x0224:
				return printMessage("WM_MDINEXT", wParam, lParam);
			case 0x0225:
				return printMessage("WM_MDIMAXIMIZE", wParam, lParam);
			case 0x0226:
				return printMessage("WM_MDITILE", wParam, lParam);
			case 0x0227:
				return printMessage("WM_MDICASCADE", wParam, lParam);
			case 0x0228:
				return printMessage("WM_MDIICONARRANGE", wParam, lParam);
			case 0x0229:
				return printMessage("WM_MDIGETACTIVE", wParam, lParam);
			case 0x0230:
				return printMessage("WM_MDISETMENU", wParam, lParam);
			case 0x0231:
				return printMessage("WM_ENTERSIZEMOVE", wParam, lParam);
			case 0x0232:
				return printMessage("WM_EXITSIZEMOVE", wParam, lParam);
			case 0x0233:
				return printMessage("WM_DROPFILES", wParam, lParam);
			case 0x0234:
				return printMessage("WM_MDIREFRESHMENU", wParam, lParam);
			case 0x0240:
				return printMessage("WM_TOUCH", wParam, lParam);
			case 0x0281:
				return printMessage("WM_IME_SETCONTEXT", wParam, lParam);
			case 0x0282:
				return printMessage("WM_IME_NOTIFY", wParam, lParam);
			case 0x0283:
				return printMessage("WM_IME_CONTROL", wParam, lParam);
			case 0x0284:
				return printMessage("WM_IME_COMPOSITIONFULL", wParam, lParam);
			case 0x0285:
				return printMessage("WM_IME_SELECT", wParam, lParam);
			case 0x0286:
				return printMessage("WM_IME_CHAR", wParam, lParam);
			case 0x0288:
				return printMessage("WM_IME_REQUEST", wParam, lParam);
			case 0x0290:
				return printMessage("WM_IME_KEYDOWN", wParam, lParam);
			case 0x0291:
				return printMessage("WM_IME_KEYUP", wParam, lParam);
			case 0x02A1:
				return printMessage("WM_MOUSEHOVER", wParam, lParam);
			case 0x02A3:
				return printMessage("WM_MOUSELEAVE", wParam, lParam);
			case 0x02A0:
				return printMessage("WM_NCMOUSEHOVER", wParam, lParam);
			case 0x02A2:
				return printMessage("WM_NCMOUSELEAVE", wParam, lParam);
			case 0x02B1:
				return printMessage("WM_WTSSESSION_CHANGE", wParam, lParam);
			case 0x02c0:
				return printMessage("WM_TABLET_FIRST", wParam, lParam);
			case 0x02df:
				return printMessage("WM_TABLET_LAST", wParam, lParam);
			case 0x0300:
				return printMessage("WM_CUT", wParam, lParam);
			case 0x0301:
				return printMessage("WM_COPY", wParam, lParam);
			case 0x0302:
				return printMessage("WM_PASTE", wParam, lParam);
			case 0x0303:
				return printMessage("WM_CLEAR", wParam, lParam);
			case 0x0304:
				return printMessage("WM_UNDO", wParam, lParam);
			case 0x0305:
				return printMessage("WM_RENDERFORMAT", wParam, lParam);
			case 0x0306:
				return printMessage("WM_RENDERALLFORMATS", wParam, lParam);
			case 0x0307:
				return printMessage("WM_DESTROYCLIPBOARD", wParam, lParam);
			case 0x0308:
				return printMessage("WM_DRAWCLIPBOARD", wParam, lParam);
			case 0x0309:
				return printMessage("WM_PAINTCLIPBOARD", wParam, lParam);
			case 0x030A:
				return printMessage("WM_VSCROLLCLIPBOARD", wParam, lParam);
			case 0x030B:
				return printMessage("WM_SIZECLIPBOARD", wParam, lParam);
			case 0x030C:
				return printMessage("WM_ASKCBFORMATNAME", wParam, lParam);
			case 0x030D:
				return printMessage("WM_CHANGECBCHAIN", wParam, lParam);
			case 0x030E:
				return printMessage("WM_HSCROLLCLIPBOARD", wParam, lParam);
			case 0x030F:
				return printMessage("WM_QUERYNEWPALETTE", wParam, lParam);
			case 0x0310:
				return printMessage("WM_PALETTEISCHANGING", wParam, lParam);
			case 0x0311:
				return printMessage("WM_PALETTECHANGED", wParam, lParam);
			case 0x0312:
				return printMessage("WM_HOTKEY", wParam, lParam);
			case 0x0317:
				return printMessage("WM_PRINT", wParam, lParam);
			case 0x0318:
				return printMessage("WM_PRINTCLIENT", wParam, lParam);
			case 0x0319:
				return printMessage("WM_APPCOMMAND", wParam, lParam);
			case 0x031A:
				return printMessage("WM_THEMECHANGED", wParam, lParam);
			case 0x031D:
				return printMessage("WM_CLIPBOARDUPDATE", wParam, lParam);
			case 0x031E:
				return printMessage("WM_DWMCOMPOSITIONCHANGED", wParam, lParam);
			case 0x031F:
				return printMessage("WM_DWMNCRENDERINGCHANGED", wParam, lParam);
			case 0x0320:
				return printMessage("WM_DWMCOLORIZATIONCOLORCHANGED", wParam, lParam);
			case 0x0321:
				return printMessage("WM_DWMWINDOWMAXIMIZEDCHANGE", wParam, lParam);
			case 0x0323:
				return printMessage("WM_DWMSENDICONICTHUMBNAIL", wParam, lParam);
			case 0x0326:
				return printMessage("WM_DWMSENDICONICLIVEPREVIEWBITMAP", wParam, lParam);
			case 0x033F:
				return printMessage("WM_GETTITLEBARINFOEX", wParam, lParam);
			case 0x0358:
				return printMessage("WM_HANDHELDFIRST", wParam, lParam);
			case 0x035F:
				return printMessage("WM_HANDHELDLAST", wParam, lParam);
			case 0x0360:
				return printMessage("WM_AFXFIRST", wParam, lParam);
			case 0x037F:
				return printMessage("WM_AFXLAST", wParam, lParam);
			case 0x0380:
				return printMessage("WM_PENWINFIRST", wParam, lParam);
			case 0x038F:
				return printMessage("WM_PENWINLAST", wParam, lParam);
			case 0x8000:
				return printMessage("WM_APP", wParam, lParam);
			default:
				return printMessage("<UNKNOWN>", wParam, lParam);
		}
	}

}