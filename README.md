# Dividend Calendar

## Description
```
보유한 주식의 배당락일 그리고 배당 지급일을 한번에 확인하기 어렵다고요?

내가 보유한 주식을 입력하면 다음 배당락일과 배당 지급일을 달력에 표시해 줍니다. 이를 통해 나만의 배당 포트폴리오를 쉽게 관리할 수 있습니다.

당신의 경제적 자유, 그날을 위한 앱!
꾸준한 배당금 투자를 통해 이루세요.
```

## Clean Architecture

- Data layer

데이터과 관련된 부분을 담당합니다.
리포지터리 패턴을 적용하여 한번 호출된 API는 캐싱해서 사용합니다.

- Presentation Layer

화면과 관련된 부분을 담당합니다.
MVVM 패턴으로 작업되어 있습니다.

> 각 레이버별로 Model을 두어 서로의 의존성을 없앴습니다. Data -> Presentation 으로의 의존성을 가지도록 설계되었습니다.

## Spec

- MVVM Architecture Pattern
- Databinding
- AAC(ViewModel, LiveData, Room)
- Repository Pattern
- Clean Architecture
- Coroutine

## Third party

- Retrofit
- Okhttp
- Glide
- [BlackJinBase](https://github.com/dlwls5201/BlackjinBase)

## API

- [iexcloud](https://iexcloud.io/)


