---
title: '프로젝트 8일: D3.js 트랜지션(Transitions)'
kind: article
date: 2014-05-14
layout: holiday-project-d3js
url: holiday-project-day-08
---

나는 30일간의 안식휴가 중이다. 휴가 동안 개인 개발 프로젝트를 진행하고 있다.

[어제]와 오늘, [D3.js API 레퍼런스](https://github.com/mbostock/d3/wiki/API-Reference)의 [Selections] 부분과 [Transitions](https://github.com/mbostock/d3/wiki/Transitions) 부분을 읽었다. [그제] 처음으로 [D3.js]를 연습해보며 대충만 추측하고 넘어갔던 부분들이 조금은 더 이해가 되기 시작했다. [Selections]의 한쪽에, ```selection#data()``` 함수를 설명하는 부분에 데이터 바인딩과 관련해서 소개된 글, [Thinking With Joins](http://bost.ocks.org/mike/join/)도 읽어보게 됐는데, 본문에 아래와 같은 내용이 있었다.

> Here’s the deal. Instead of telling D3 how to do something, tell D3 what you want. You want the circle elements to correspond to data. You want one circle per datum. Instead of instructing D3 to create circles, then, tell D3 that the selection "circle" should correspond to data. This concept is called the data join:

"D3에게 무언가 어떻게 하라고 말하지 말고, 무엇을 원하는지를 말하라"는 부분이 공명을 일으켰다. 일일이 어떻게 하라고 지시하는 것이 아니라, 무엇을 원하니 알아서 해보라는 것, 일일이 사사건건 지시하는 것보다 훨씬 간결한 방법이다. 이 부분을 읽고 [D3.js]에 호감이 더 커졌다. 연상된 딴생각으로, 프로그래밍뿐만 아니라 회사에서 일할 때 업무를 전달하고 과정을 점검할 때도 참고할만한 내용이라 생각한다. 일을 어떻게 할지를 사사건건 간섭하는 상관이 있으면, 서로 피곤할 뿐만 아니라, 업무 진행도 잘 안 되지 않던가?!

주제로 돌아와서, [Transitions]의 내용은, 쉽게 생각하면 애니메이션을 넣는 함수들이 설명된 것인데, 시작 값과 최종 값이 있는 상황에서 중간 값을 처리하는 interpolation 함수를 지정하거나 직접 정의해서 DOM Element를 이동하거나, 속성값 변화를 시간을 두고 진행하는 것이다.

```js
d3.select("body").transition().style("color", "red");
```

간단한 예제로, 위의 D3 코드는, ```body``` 요소의 스타일 중 색상을 기존 색상에서 ```red``` 색상으로 바꾸는 트랜지션 코드인데, 중간에 ```transition()```이라는 구분이 없었으면 실행 즉시 빨간색으로 바뀌었겠지만, 트랜지션 요소를 넣었을 때에는 시간을 두고 서서히 반영된다. 곧바로 빨간색이 되는 것이 아니라, 기존 색에서 점점 더 빨간색으로 바뀌면서 마지막에 ```red```색이 되는 것이다. 시간의 길이도 지정할 수 있고, 기본은 250ms 동안 진행된다.

[그제] D3.js로 힘들여(?) 그린 그래프에 트랜지션을 넣어서 살짝 연습을 해봤다. 아래 그래프의 원을 클릭하면 해당 원의 지름이 커졌다가 작아지는 효과를 보인다.

[어제]: /holiday-project-day-07/
[그제]: /holiday-project-day-06/
[Selections]: https://github.com/mbostock/d3/wiki/Selections
[Transitions]: https://github.com/mbostock/d3/wiki/Transitions
[D3.js]: http://d3js.org/

<style>
  circle {
    stroke: black;
    stroke-width: 1.5px;
    fill: steelblue;
    cursor: pointer;
  }
  text {
    fill: white;
    text-anchor: middle;
    cursor: pointer;
  }
  path {
    stroke: darkgrey;
    stroke-width: 2px;
    fill: none;
  }
</style>
<svg class="d3"></svg>

<script>
  var points = [[60, 140], [200, 60], [400, 230]];
  var textVal = ["가", "나", "다"];

  var chart = d3.select("svg.d3")
    .attr("width", 500)
    .attr("height", 300)

  var line = d3.svg.line()
    .interpolate("monotone");

  var path = chart.append("path")
    .attr("class", "line")
    .attr("d", line(points));

  var g = chart.selectAll("g")
    .data(points)
    .enter()
    .append("g")
      .attr("transform", function(d, i) {
        return "translate(" + d[0] + ", " + d[1] + ")";
      });

  var circle = g.append("circle").attr("r", 30);

  var text = g.append("text")
    .attr("dy", ".35em")
    .text(function(d, i) { return textVal[i]; });

  g.on('click', function() {
    d3.select(this).selectAll("circle")
      .transition()
        .ease("quad")
        .duration(700)
        .attr("r", 50)
      .transition()
        .ease("bounce")
        .duration(2000)
        .attr("r", 30);
  });
</script>

> **화면의 "가", "나", "다" 아무 원이나 눌러보자!**

[그제] 그린 상태에서 아래 코드를 추가했다.

```js
g.on('click', function() {
  d3.select(this).selectAll("circle")
    .transition()
      .ease("quad")
      .duration(700)
      .attr("r", 50)
    .transition()
      .ease("bounce")
      .duration(2000)
      .attr("r", 30);
});
```

각각의 SVG ```circle``` 태그를 감싸고 있는 ```g``` 태그가 눌리면(click 이벤트 발생), 트랜지션 두개를 연달아 실행하는 코드이다. 먼저 700ms에 걸쳐 반지름을 50으로 늘렸다가, 2초에 걸쳐 다시 30으로 줄인다. ```ease```는 그 중간값들을 어떻게 처리할 것인지 지정하는 것인데, [미리 정의된 빌트인 함수](https://github.com/mbostock/d3/wiki/Transitions#d3_ease)를 쓸 수 있다.

이렇게 D3.js를 살짝 더 공부해 보았다.

결국, 프로젝트에서 원하는 기능은, graphviz로 그린 그래프와 비슷한 모습으로 웹페이지에 그려놓고, 새로 추가되는 노드들을 그 자리에서 새로 만들어 보여주는 것인데, graphviz로는 새로 그려서 전체를 다시 보여줘야 할 것 같지만, D3.js로 그린다면, 동적으로 노드를 만들어서 보여줄 수 있을 것 같다.

한편, 처음 만드는 모습부터 반드시 "동적"으로 추가될 필요까지는 없을 것 같아서, 그냥 graphviz로 진행하는 것이 빠르고 편할지도 모르겠다. 이런 고민은 프로젝트를 진행하다 보면 자주 하는 고민이다. "이걸로 할까? 저걸로 할까? 이렇게 하는 게 옳은 걸까? 저렇게 하는 게 맞는 걸까?"

보통, 그럴 때 답은

> 그냥 뭐가 됐든 일단 해봐! 해봐야 뭐가 맞는 지 알지!

인 경우가 많다. 그 점을 경험상 알고 있지만, 그럼에도 우유부단해서 결정을 못하게 되는데, 오늘도 마찬가지 상황이다.

일단, 오늘은 여기까지.


[그제]: /holiday-project-day-06/
