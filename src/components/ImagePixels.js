import {View, Text} from 'react-native';
import React, {useEffect, useState} from 'react';
import PixelsImage from '../../PixelsImage';

const ImagePixels = () => {
  useEffect(() => {
    
  }, [])
  
  function testFunction() {
    PixelsImage.createBinaryPixels('testName', 'testLocation');
    console.log("goooooooooodddddddd");
  }

  return (
    <View style={{
      justifyContent: 'center',
      flex: 1,
      alignItems: 'center',
    }}>
      <Text style={{
        fontSize: 20,
        backgroundColor: 'tomato',
        padding: 10,
        borderRadius: 20,
      }} onPress={() => testFunction()}>clique</Text>
    </View>
  );
};

export default ImagePixels;
