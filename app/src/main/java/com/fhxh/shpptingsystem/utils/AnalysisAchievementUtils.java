package com.fhxh.shpptingsystem.utils;

import android.util.Log;

import com.fhxh.shpptingsystem.ui.bean.PaintXY;

import java.util.List;

public class AnalysisAchievementUtils {

    private static String TAG = "AnalysisAchievementUtils";

    public static int[] setEvement(List<PaintXY> analysis, double ringNumber) {
        int[] achieves = new int[5];
        if (ringNumber == 0) {
            achieves[0] = 0;
            achieves[1] = 0;
            achieves[2] = 0;
            achieves[3] = 0;
            achieves[4] = 0;
            return achieves;
        }

        /**
         * 据枪 获取击发前2秒40个点的位置，当前点减去前一个点，获取到行程，根据行程来判断成绩
         */
        if (analysis.size() > 25) {
            int xThem = 0;
            int yThem = 0;
            for (int i = 1; i < 25; i++) {
                int x1 = (int) (analysis.get(analysis.size() - 1).getX() * 100);
                int y1 = (int) (analysis.get(analysis.size() - 1).getY() * 100);
                int x2 = (int) (analysis.get(analysis.size() - i - 1).getX() * 100);
                int y2 = (int) (analysis.get(analysis.size() - i - 1).getY() * 100);
                if (x1 > x2) {
                    int xxThem = x1 - x2;
                    xThem = xThem + xxThem;
                } else {
                    int xxThem = x2 - x1;
                    xThem = xThem + xxThem;
                }
                if (y1 > y2) {
                    int yyThem = y1 - y2;
                    yThem = yThem + yyThem;
                } else {
                    int yyThem = y2 - y1;
                    yThem = yThem + yyThem;
                }
            }
            int x = xThem / 40;
            int y = yThem / 40;
            int xy = (x + y) / 2;
            if (xy>10){
                achieves[0] = 50;
            }else {
                achieves[0] = 100-(xy*5);
            }

        } else {
            int xThem = 0;
            int yThem = 0;
            for (int i = 1; i < analysis.size(); i++) {
                int x1 = (int) (analysis.get(analysis.size() - 1).getX() * 100);
                int y1 = (int) (analysis.get(analysis.size() - 1).getY() * 100);
                int x2 = (int) (analysis.get(analysis.size() - i - 1).getX() * 100);
                int y2 = (int) (analysis.get(analysis.size() - i - 1).getY() * 100);
                if (x1 > x2) {
                    int xxThem = x1 - x2;
                    xThem = xThem + xxThem;
                } else {
                    int xxThem = x2 - x1;
                    xThem = xThem + xxThem;
                }
                if (y1 > y2) {
                    int yyThem = y1 - y2;
                    yThem = yThem + yyThem;
                } else {
                    int yyThem = y2 - y1;
                    yThem = yThem + yyThem;
                }

            }
            int x = xThem / analysis.size();
            int y = yThem / analysis.size();
            int xy = (x + y) / 2;

            if (xy>10){
                achieves[0] = 50;
            }else {
                achieves[0] = 100-(xy*5);
            }


        }

        /**
         * 瞄准
         * 击发前2秒  以10环为中心，算成绩 瞄准离中心越远分数越低，脱靶位0
         */
        if (analysis.size() > 40) {
            int xThem = 0;
            int yThem = 0;
            for (int i = 1; i < 40; i++) {
                int x = (int) (analysis.get(analysis.size() - i).getX() * 100);
                int y = (int) (analysis.get(analysis.size() - i).getY() * 100);
                if (x > 50) {
                    int xxThem = x - 50;
                    xThem = xThem + xxThem;
                } else {
                    int xxThem = 50 - x;
                    xThem = xThem + xxThem;
                }

                if (y > 60) {
                    int yyThem = y - 60;
                    yThem = yThem + yyThem;
                } else {
                    int yyThem = 60 - y;
                    yThem = yThem + yyThem;
                }
            }
            int x = xThem / 40;
            int y = yThem / 40;
            achieves[1] = 50;
            if (x < 5 && y < 5) {
                achieves[1] = 100;
            }
            if (x >= 5 && x < 10) {
                achieves[1] = 90;
                if (y >= 10 && y < 20) {
                    achieves[1] = 80;
                }
                if (y >= 20 && y < 30) {
                    achieves[1] = 70;
                }
                if (y >= 30 && y < 40) {
                    achieves[1] = 60;
                }
                if (y >= 40 && y < 50) {
                    achieves[1] = 50;
                }
                if (y >= 50) {
                    achieves[1] = 40;
                }
            }

            if (x >= 10 && x < 20) {
                achieves[1] = 80;
                if (y >= 20 && y < 30) {
                    achieves[1] = 70;
                }
                if (y >= 30 && y < 40) {
                    achieves[1] = 60;
                }
                if (y >= 40 && y < 50) {
                    achieves[1] = 50;
                }
                if (y >= 50) {
                    achieves[1] = 40;
                }
            }
            if (x >= 20 && x < 30) {
                achieves[1] = 70;
                if (y >= 30 && y < 40) {
                    achieves[1] = 60;
                }
                if (y >= 40 && y < 50) {
                    achieves[1] = 50;
                }
                if (y >= 50) {
                    achieves[1] = 40;
                }
            }
            if (x >= 30 && x < 40) {
                achieves[1] = 60;
                if (y >= 40 && y < 50) {
                    achieves[1] = 50;
                }
                if (y >= 50) {
                    achieves[1] = 40;
                }
            }
            if (x >= 40 && x < 50) {
                achieves[1] = 60;
                if (y >= 50) {
                    achieves[1] = 40;
                }
            }


        } else {
            int xThem = 0;
            int yThem = 0;
            for (int i = 1; i < analysis.size(); i++) {
                int x = (int) (analysis.get(analysis.size() - i).getX() * 100);
                int y = (int) (analysis.get(analysis.size() - i).getY() * 100);
                if (x > 50) {
                    int xxThem = x - 50;
                    xThem = xThem + xxThem;
                } else {
                    int xxThem = 50 - x;
                    xThem = xThem + xxThem;
                }

                if (y > 60) {
                    int yyThem = y - 60;
                    yThem = +yThem + yyThem;
                } else {
                    int yyThem = 60 - y;
                    yThem = +yThem + yyThem;
                }
            }
            int x = xThem / analysis.size();
            int y = yThem / analysis.size();

            achieves[1] = 50;
            if (x < 5 && y < 5) {
                achieves[1] = 100;
            }
            if (x >= 5 && x < 10) {
                achieves[1] = 90;
                if (y >= 10 && y < 20) {
                    achieves[1] = 80;
                }
                if (y >= 20 && y < 30) {
                    achieves[1] = 70;
                }
                if (y >= 30 && y < 40) {
                    achieves[1] = 60;
                }
                if (y >= 40 && y < 50) {
                    achieves[1] = 50;
                }
                if (y >= 50) {
                    achieves[1] = 40;
                }
            }

            if (x >= 10 && x < 20) {
                achieves[1] = 80;
                if (y >= 20 && y < 30) {
                    achieves[1] = 70;
                }
                if (y >= 30 && y < 40) {
                    achieves[1] = 60;
                }
                if (y >= 40 && y < 50) {
                    achieves[1] = 50;
                }
                if (y >= 50) {
                    achieves[1] = 40;
                }
            }
            if (x >= 20 && x < 30) {
                achieves[1] = 70;
                if (y >= 30 && y < 40) {
                    achieves[1] = 60;
                }
                if (y >= 40 && y < 50) {
                    achieves[1] = 50;
                }
                if (y >= 50) {
                    achieves[1] = 40;
                }
            }
            if (x >= 30 && x < 40) {
                achieves[1] = 60;
                if (y >= 40 && y < 50) {
                    achieves[1] = 50;
                }
                if (y >= 50) {
                    achieves[1] = 40;
                }
            }
            if (x >= 40 && x < 50) {
                achieves[1] = 60;
                if (y >= 50) {
                    achieves[1] = 40;
                }
            }

        }


        /**
         * 击发
         *
         * 只要上靶为60
         *
         *
         */
        if (analysis.size() > 6) {
            int xThem = 0;
            int yThem = 0;
            for (int i = 1; i < 6; i++) {
                int x1 = (int) (analysis.get(analysis.size() - i).getX() * 100);
                int y1 = (int) (analysis.get(analysis.size() - i).getY() * 100);
                int x2 = (int) (analysis.get(analysis.size() - i - 1).getX() * 100);
                int y2 = (int) (analysis.get(analysis.size() - i - 1).getY() * 100);
                int xxThem;
                if (x1 > x2) {
                    xxThem = x1 - x2;
                } else {
                    xxThem = x2 - x1;
                }
                xThem = xThem + xxThem;
                int yyThem;
                if (y1 > y2) {
                    yyThem = y1 - y2;
                } else {
                    yyThem = y2 - y1;
                }
                yThem = yThem + yyThem;

            }
            int xy = xThem+yThem;

            if (xy>20){
                achieves[2] = 60;
            }else {
                achieves[2] = 98-(xy*2);
            }
        } else {
            int xThem = 0;
            int yThem = 0;
            for (int i = 1; i < analysis.size(); i++) {
                int x1 = (int) (analysis.get(analysis.size() - i).getX() * 100);
                int y1 = (int) (analysis.get(analysis.size() - i).getY() * 100);
                int x2 = (int) (analysis.get(analysis.size() - i - 1).getX() * 100);
                int y2 = (int) (analysis.get(analysis.size() - i - 1).getY() * 100);
                if (x1 > x2) {
                    xThem = x1 - x2;
                } else {
                    xThem = x2 - x1;
                }
                if (y1 > y2) {
                    yThem = y1 - y2;
                } else {
                    yThem = y2 - y1;
                }

            }
            int xy = xThem+yThem;
            if (xy>20){
                achieves[2] = 60;
            }else {
                achieves[2] = 98-(xy*2);
            }
        }

        /**
         * 成绩
         */
        if (ringNumber >= 10) {
            achieves[3] = 100;
        }
        if (ringNumber >= 9 && ringNumber < 10) {
            achieves[3] = 90;
        }
        if (ringNumber >= 8 && ringNumber < 9) {
            achieves[3] = 80;
        }
        if (ringNumber >= 7 && ringNumber < 8) {
            achieves[3] = 70;
        }
        if (ringNumber >= 6 && ringNumber < 7) {
            achieves[3] = 60;
        }
        if (ringNumber >= 5 && ringNumber < 6) {
            achieves[3] = 50;
        }
        if (ringNumber < 5) {
            achieves[3] = 0;
        }


        /**
         * 总体
         */
        achieves[4] = (achieves[0] + achieves[1] + achieves[2] + achieves[3]) / 4;

        return achieves;
    }

}
