<anychart>
  <charts>
    <chart plot_type="CategorizedHorizontal">
      <data>	
		<series name="Series 1">
			<#list data as var>
				<point name="${var[1]}" y="${var[0]}"/>
			</#list>
		</series>
      </data>
      <chart_settings>
        <title>
          <text>Кол-во оценок по ученикам</text>
        </title>
        <axes>
          <y_axis>
            <title>
              <text>Кол-во оценок</text>
            </title>
          </y_axis>
          <x_axis>
            <labels align="Outside"/>
            <title>
              <text>Ученик</text>
            </title>
          </x_axis>
        </axes>
      </chart_settings>
    </chart>
  </charts>
</anychart>