package racingcar.domain.game.car;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import racingcar.domain.dto.CarMovementDto;
import racingcar.domain.dto.CarsMovementDto;
import racingcar.domain.dto.WinnersDto;

class CarsTest {
    private Cars cars;

    @BeforeEach
    void setUp() {
        Car audi = Car.from(CarName.from("audi"));
        Car benz = Car.from(CarName.from("benz"));
        Car bmw = Car.from(CarName.from("bmw"));

        List<Car> carList = List.of(audi, benz, bmw);
        cars = Cars.from(carList);
    }

    @DisplayName("Cars 객체를 생성할 수 있는지 검증")
    @Test
    void should_Create_Cars() {
        assertThat(cars).isNotNull();
    }

    @DisplayName("Car가 전진 조건을 만족해서 움직인다면, 각 자동차의 position은 1이어야 한다")
    @Test
    void should_Move_Cars_If_Condition_Met() {
        // given
        FixedMoveStrategy fixedMoveStrategy = new FixedMoveStrategy(true);

        // when
        cars.moveOnceIfMovable(fixedMoveStrategy);

        // then
        for (Car car : cars.getCars()) {
            assertThat(car.getPosition()).isEqualTo(1);
        }
    }

    @DisplayName("Car가 전진 조건을 만족하지 않는다면, 각 자동차의 position은 0이어야 한다")
    @Test
    void should_Not_Move_Cars_If_Condition_Not_Met() {
        // given
        FixedMoveStrategy fixedMoveStrategy = new FixedMoveStrategy(false);

        // when
        cars.moveOnceIfMovable(fixedMoveStrategy);

        // then
        for (Car car : cars.getCars()) {
            assertThat(car.getPosition()).isEqualTo(0);
        }
    }

    @DisplayName("CarsMovementDto로 변환 시 유효한 데이터를 제공하는지 확인")
    @Test
    void should_Provide_Valid_CarsMovementDto() {
        // given
        List<CarMovementDto> carsMovementDto = cars.getCars().stream()
                .map(Car::toCarMovementDto)
                .toList();
        CarsMovementDto expected = new CarsMovementDto(carsMovementDto);

        // when
        CarsMovementDto result = cars.toCarsMovementDto();

        // then
        assertThat(expected).isEqualTo(result);
    }

    @DisplayName("최종 우승자를 가려내는지 검증")
    @Test
    void should_Determine_Final_Winners() {
        // given
        cars.getCars().get(0).moveOnce();
        cars.getCars().get(1).moveOnce();

        // when
        WinnersDto winnersDto = cars.toWinnerDto();

        // then
        assertThat(winnersDto).isNotNull();
        assertThat(winnersDto.winners()).isEqualTo("audi, benz");
    }
}