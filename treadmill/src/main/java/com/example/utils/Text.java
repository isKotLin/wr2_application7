package com.example.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 心率滑动选择器
 */
public class Text {                  //最大值,默认值,最小值
	private static int[][] vausle = {
			{195, 123, 123},//15岁
			{194, 122, 122},//16岁
			{193, 122, 122},//17岁
			{192, 121, 121},//18岁
			{191, 121, 121},//19岁
			{190, 120, 120},//20岁
			{189, 119, 119},//21岁
			{188, 119, 119},//22岁
			{187, 118, 118},//23岁
			{186, 118, 118},//24岁
			{185, 117, 117},//25岁
			{184, 116, 116},//26岁
			{183, 116, 116},//27岁
			{182, 115, 115},//28岁
			{181, 115, 115},//29岁
			{181, 114, 114},//30岁
			{180, 113, 113},//31岁
			{179, 113, 113},//32岁
			{178, 112, 112},//33岁
			{177, 112, 112},//34岁
			{176, 111, 111},//35岁
			{175, 110, 110},//36岁
			{174, 110, 110},//37岁
			{173, 109, 109},//38岁
			{172, 109, 109},//39岁
			{171, 108, 108},//40岁
			{170, 107, 107},//41岁
			{169, 107, 107},//42岁
			{168, 106, 106},//43岁
			{167, 106, 106},//44岁
			{166, 105, 105},//45岁
			{165, 104, 104},//46岁
			{164, 103, 103},//47岁
			{163, 103, 103},//48岁
			{162, 103, 103},//49岁
			{162, 102, 102},//50岁
			{161, 101, 101},//51岁
			{160, 101, 101},//52岁
			{159, 100, 100},//53岁
			{158, 100, 100},//54岁
			{157, 99, 99},//55岁
			{156, 98, 98},//56岁
			{155, 98, 98},//57岁
			{154, 97, 97},//58岁
			{153, 97, 97},//59岁
			{152, 96, 96},//60岁
			{151, 95, 95},//61岁
			{150, 95, 95},//62岁
			{149, 94, 94},//63岁
			{148, 94, 94},//64岁
			{147, 93, 93},//65岁
			{146, 92, 92},//66岁
			{145, 92, 92},//67岁
			{144, 91, 91},//68岁
			{143, 91, 91},//69岁
			{143, 90, 90},//70岁
			{142, 90, 89},//71岁
			{141, 90, 89},//72岁
			{140, 90, 88},//73岁
			{139, 90, 88},//74岁
			{138, 90, 87},//75岁
			{137, 90, 86},//76岁
			{136, 90, 86},//77岁
			{135, 90, 85},//78岁
			{134, 90, 85},//79岁
			{133, 90, 84},//80岁
	};

	static List<String> str = new ArrayList<>();

	public static List<String> getArray(int index) {//得到集合
//		Log.e("TAG", "----------------------------index:" + index);
		str.clear();
		for (int i = vausle[index][2]; i <= vausle[index][0]; i++) {//得到最大值和最小值
			str.add(i < 100 ? "0" + i : i + "");
			System.out.println(i < 100 ? "0" + i : i + "");

		}
		return str;
	}

	public static int getIndex(int index) {//得到默认值
		for (int i = 0; i < str.size(); i++) {
			if (Integer.parseInt(str.get(i)) == vausle[index][1])
				return i;
		}
		return 0;
	}
//    public static String[] speedss(int index) {
//        getDefule(vausle[index][1], getStrings(vausle[index][0], vausle[index][2]));
//        return getStrings(vausle[index][0], vausle[index][2]);
//    }
//
//    public static int slpoesss(int index){
//        return getDefule(vausle[index][1], getStrings(vausle[index][0], vausle[index][2]));
//    }
//
//    private static String[] getStrings(int max, int min) {
//        String[] str  = new String[max - min + 1];
//        int j = 0;
//        for (; min <= max; j++, min++) {
//            str[j] = min + "";
//        } return str;}
//
//    private static int getDefule(int defule, String[] str) {
//        for (int i = 0; i < str.length; i++) {
//            String string = str[i];
//            if (string.equals(defule + ""))
//                return i;
//        }
//        return 0;
//    }
}
