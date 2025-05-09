import React, { useContext } from 'react';
import { StoreContext } from '../../context/StoreContext';
import FoodItem from '../FoodItem/FoodItem';

const FoodDisplay = ({ category = 'All', searchText = '' }) => {
  const { foodList } = useContext(StoreContext);

  console.log("Food List from StoreContext:", foodList);
  console.log("Current Category:", category);
  console.log("Current Search Text:", searchText);

  const filteredFoods = foodList.filter(food => (
    (category === 'All' || food.category === category) &&
    food.name.toLowerCase().includes(searchText.toLowerCase())
  ));

  console.log("Filtered Foods to display:", filteredFoods);

  return (
    <div className="container">
      <div className="row">
        {filteredFoods.length > 0 ? (
          filteredFoods.map((food, index) => (
            <FoodItem
              key={index}
              name={food.name}
              description={food.description}
              id={food.id}
              imageURL={food.imageURL}
              price={food.price}
            />
          ))
        ) : (
          <div className="text-center mt-4">
            <h4>No food found.</h4>
          </div>
        )}
      </div>
    </div>
  );
}

export default FoodDisplay;
