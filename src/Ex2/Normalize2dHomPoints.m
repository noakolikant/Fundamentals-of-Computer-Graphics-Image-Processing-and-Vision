function [ normalized_points, T ] = Normalize2dHomPoints( points )
%Normalize2dHomPoints 
    points_means = mean(points(:, 1:2));
    points_t_translate = [1, 0 , -points_means(1); ...
        0, 1, -points_means(2); 0, 0, 1];
    hom_points = points_t_translate * points';
    points_distance_vector = sqrt(hom_points(1,:).^2 + hom_points(2,:).^2);
    points_mean_distance = mean(points_distance_vector(:));
    points_scale_factor = sqrt(2) / points_mean_distance;
    points_t_scale = [points_scale_factor 0 0; 0 points_scale_factor 0; ...
        0 0 1];
    normalized_points = points_t_scale * hom_points;
    T = points_t_scale * points_t_translate;

end

