from collections import *

new_states = [
    [0, 1, 6, 11, 16, 23, 26, 31, 36, 39, 45, 51, 58, 63, 68, 74, 80, 84, 88, 91, 94, 101, 105, 109, 114, 118, 122, 127, 134, 135, 138, 144, 147, 156, 176, 178, 180, 182, 184, 186, 188, 190, 193],
    [85, 102, 115, 148, 149, 150, 152, 155],
    [123, 148, 149, 150, 152, 155],
    [59, 148, 149, 150, 152, 155],
    [37, 148, 149, 150, 152, 155],
    [32, 92, 148, 149, 150, 152, 155],
    [69, 148, 149, 150, 152, 155],
    [148, 149, 150, 152, 155],
    [7, 148, 149, 150, 152, 155],
    [24, 75, 148, 149, 150, 152, 155],
    [95, 148, 149, 150, 152, 155],
    [2, 110, 148, 149, 150, 152, 155],
    [81, 119, 148, 149, 150, 152, 155],
    [52, 89, 148, 149, 150, 152, 155],
    [12, 148, 149, 150, 152, 155],
    [17, 148, 149, 150, 152, 155],
    [106, 128, 148, 149, 150, 152, 155],
    [27, 64, 148, 149, 150, 152, 155],
    [40, 148, 149, 150, 152, 155],
    [46, 148, 149, 150, 152, 155],
    [158, 173],
    [177],
    [179],
    [181],
    [183],
    [185],
    [187],
    [189],
    [191],
    [194],
    [136],
    [145, 146],
    [139, 140, 141, 143, 146],
    [149, 150, 151, 152, 154, 155],
    [103, 149, 150, 151, 152, 154, 155],
    [86, 149, 150, 151, 152, 154, 155],
    [116, 149, 150, 151, 152, 154, 155],
    [149, 150, 152, 153, 154, 155],
    [124, 149, 150, 151, 152, 154, 155],
    [60, 149, 150, 151, 152, 154, 155],
    [38, 149, 150, 151, 152, 154, 155],
    [33, 149, 150, 151, 152, 154, 155],
    [93, 149, 150, 151, 152, 154, 155],
    [70, 149, 150, 151, 152, 154, 155],
    [8, 149, 150, 151, 152, 154, 155],
    [25, 149, 150, 151, 152, 154, 155],
    [76, 149, 150, 151, 152, 154, 155],
    [96, 149, 150, 151, 152, 154, 155],
    [3, 149, 150, 151, 152, 154, 155],
    [111, 149, 150, 151, 152, 154, 155],
    [82, 149, 150, 151, 152, 154, 155],
    [120, 149, 150, 151, 152, 154, 155],
    [90, 149, 150, 151, 152, 154, 155],
    [53, 149, 150, 151, 152, 154, 155],
    [13, 149, 150, 151, 152, 154, 155],
    [18, 149, 150, 151, 152, 154, 155],
    [129, 149, 150, 151, 152, 154, 155],
    [107, 149, 150, 151, 152, 154, 155],
    [28, 149, 150, 151, 152, 154, 155],
    [65, 149, 150, 151, 152, 154, 155],
    [41, 149, 150, 151, 152, 154, 155],
    [47, 149, 150, 151, 152, 154, 155],
    [159, 173],
    [174],
    [192],
    [137, 140, 141, 143, 146],
    [141, 142, 143, 146],
    [104, 149, 150, 151, 152, 154, 155],
    [87, 149, 150, 151, 152, 154, 155],
    [117, 149, 150, 151, 152, 154, 155],
    [125, 149, 150, 151, 152, 154, 155],
    [61, 149, 150, 151, 152, 154, 155],
    [34, 149, 150, 151, 152, 154, 155],
    [71, 149, 150, 151, 152, 154, 155],
    [9, 149, 150, 151, 152, 154, 155],
    [77, 149, 150, 151, 152, 154, 155],
    [97, 149, 150, 151, 152, 154, 155],
    [4, 149, 150, 151, 152, 154, 155],
    [112, 149, 150, 151, 152, 154, 155],
    [83, 149, 150, 151, 152, 154, 155],
    [121, 149, 150, 151, 152, 154, 155],
    [54, 149, 150, 151, 152, 154, 155],
    [14, 149, 150, 151, 152, 154, 155],
    [19, 149, 150, 151, 152, 154, 155],
    [130, 149, 150, 151, 152, 154, 155],
    [108, 149, 150, 151, 152, 154, 155],
    [29, 149, 150, 151, 152, 154, 155],
    [66, 149, 150, 151, 152, 154, 155],
    [42, 149, 150, 151, 152, 154, 155],
    [48, 149, 150, 151, 152, 154, 155],
    [160, 173],
    [126, 149, 150, 151, 152, 154, 155],
    [62, 149, 150, 151, 152, 154, 155],
    [35, 149, 150, 151, 152, 154, 155],
    [72, 149, 150, 151, 152, 154, 155],
    [10, 149, 150, 151, 152, 154, 155],
    [78, 149, 150, 151, 152, 154, 155],
    [98, 149, 150, 151, 152, 154, 155],
    [5, 149, 150, 151, 152, 154, 155],
    [113, 149, 150, 151, 152, 154, 155],
    [55, 149, 150, 151, 152, 154, 155],
    [15, 149, 150, 151, 152, 154, 155],
    [20, 149, 150, 151, 152, 154, 155],
    [131, 149, 150, 151, 152, 154, 155],
    [30, 149, 150, 151, 152, 154, 155],
    [67, 149, 150, 151, 152, 154, 155],
    [43, 149, 150, 151, 152, 154, 155],
    [49, 149, 150, 151, 152, 154, 155],
    [161, 173],
    [73, 149, 150, 151, 152, 154, 155],
    [79, 149, 150, 151, 152, 154, 155],
    [99, 149, 150, 151, 152, 154, 155],
    [56, 149, 150, 151, 152, 154, 155],
    [21, 149, 150, 151, 152, 154, 155],
    [132, 149, 150, 151, 152, 154, 155],
    [44, 149, 150, 151, 152, 154, 155],
    [50, 149, 150, 151, 152, 154, 155],
    [162, 173],
    [100, 149, 150, 151, 152, 154, 155],
    [57, 149, 150, 151, 152, 154, 155],
    [22, 149, 150, 151, 152, 154, 155],
    [133, 149, 150, 151, 152, 154, 155],
    [163, 173],
    [164, 173],
    [165, 173],
    [166, 173],
    [167, 173],
    [168, 173],
    [169, 173],
    [170, 173],
    [171, 173],
    [172, 173],
    [173]
]

# c = Counter(map(tuple,new_states))
# dups = [k for k,v in c.items() if v>1]
#
# print(dups)
#
# i = len(new_states)-1
# print(str(i) + " " +str(new_states[i]))

accepting = []
non_accepting = []
counter = 0
for i in new_states:
    if (5 in i) or (10 in i) or (15 in i) or (22 in i) or (25 in i) or (30 in i) or (35 in i) or (38 in i) or (44 in i) or (50 in i) or (57 in i) or (62 in i) or (67 in i) or (73 in i) or (79 in i) or (83 in i) or (87 in i) or (90 in i) or (93 in i) or (100 in i) or (104 in i) or (108 in i) or (113 in i) or (117 in i) or (121 in i) or (126 in i) or (133 in i) or (146 in i) or (155 in i) or (174 in i) or (177 in i) or (179 in i) or (181 in i) or (183 in i) or (185 in i) or (187 in i) or (189 in i) or (192 in i) or (194 in i):
        accepting.append(i)
    else:
        non_accepting.append(i)
        print(counter)
    counter = counter + 1
# for i in accepting:
#     print(i)
