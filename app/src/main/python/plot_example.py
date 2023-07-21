import io
# import os
# import sqlite3
import matplotlib.pyplot as plt
import numpy as np

def plot(x_res_list, y_res_list, x_axis, y_axis):

#     package_dir = os.path.abspath(os.path.dirname(db_name + ".db"))
#     db_dir = os.path.join(package_dir, db_name + ".db")
#
#     con = sqlite3.connect(db_dir)
#     cursor = con.cursor()
#
#     query_string = "SELECT " + x + "," + y + " FROM " + db_name
#
#     res = cursor.execute(query_string)
#     #query_results = res.fetch_all()

#     x_res_list = []
#     y_res_list = []
#
#     for item in res:
#         x_res_list.append(item[0])
#         y_res_list.append(item[1])

    x_res = np.array(x_res_list)
    y_res = np.array(y_res_list)

#     con.close()

    fig, ax = plt.subplots()
    ax.set_xlabel(x_axis)
    ax.set_ylabel(y_axis)
    ax.grid()
    ax.plot(x_res, y_res)

    f = io.BytesIO()
    plt.savefig(f, format = "png")
    return f.getvalue()