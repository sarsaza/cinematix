<?xml version="1.0" encoding="UTF-8"?><anychart><settings><animation enabled="True" /></settings><charts><chart plot_type="CategorizedVertical"><data_plot_settings default_series_type="Bar"><bar_series point_padding="1" group_padding="1" style="AquaLight"><bar_style><hatch_fill enabled="true" type="%HatchType" color="White" thickness="1" pattern_size="2" opacity="0.5" /></bar_style><tooltip_settings enabled="True"><format> <![CDATA[{%Name} GPA: {%YValue}]]></format></tooltip_settings></bar_series></data_plot_settings><chart_settings><title enabled="true"><text>${chartName}</text></title><axes><x_axis tickmarks_placement="Center"><labels display_mode="Rotated" rotation="90" align="inside" allow_overlap="true" /><title enabled="False" /></x_axis><y_axis><title enabled="False" /><axis_markers><ranges><range minimum="0" maximum="2"><label enabled="True" rotation="90"><format><![CDATA[Неуд.]]></format><font bold="True" color="Red" /></label><fill color="Red" opacity="0.5" /><minimum_line enabled="False" /><maximum_line enabled="False" /></range><range minimum="2" maximum="3"><label enabled="True" rotation="90"><format><![CDATA[Уд.]]></format><font bold="True" color="DarkColor(Gold)" /></label><fill color="Gold" opacity="0.7" /><minimum_line enabled="False" /><maximum_line enabled="False" /></range><range minimum="3" maximum="4"><label enabled="True" rotation="90"><format><![CDATA[Хор.]]></format><font bold="True" color="DarkColor(Gold)" /></label><fill color="Gold" opacity="0.3" /><minimum_line enabled="False" /><maximum_line enabled="False" /></range><range minimum="4" maximum="5"><label enabled="True" rotation="90"><format><![CDATA[Отл.]]></format><font bold="True" color="Green" /></label><fill color="Green" opacity="0.4" /><minimum_line enabled="False" /><maximum_line enabled="False" /></range></ranges></axis_markers></y_axis></axes></chart_settings><data><series name="Series 1"><#list classAverage as ca><point name="${ca[2]}" y="${ca[1]}" hatch_type="${hatchType}"><attributes><attribute name="var">${ca[0]}</attribute></attributes></point></#list></series><series name="Bubble series" type="Bubble"><point name="${back}" y="${backValue}" size="1"><attributes><attribute name="var">${back}</attribute></attributes></point></series></data></chart></charts></anychart>